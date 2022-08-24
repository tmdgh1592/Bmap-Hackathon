package com.app.hackathon.presentation.view.map

import android.Manifest
import android.content.Intent
import android.location.Location
import android.os.Looper
import android.util.Log
import android.view.View
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.annotation.UiThread
import com.app.hackathon.presentation.R
import com.app.hackathon.presentation.base.BaseActivity
import com.app.hackathon.presentation.databinding.ActivityMapBinding
import com.app.hackathon.presentation.presenter.MapContract
import com.app.hackathon.presentation.presenter.MapPresenter
import com.app.hackathon.presentation.view.search.VoiceSearchActivity
import com.app.hackathon.presentation.widget.Constants
import com.app.hackathon.presentation.widget.extensions.checkLocationPermission
import com.app.hackathon.presentation.widget.extensions.loadImage
import com.app.hackathon.presentation.widget.extensions.setStatusBarTransparent
import com.app.hackathon.presentation.widget.extensions.statusBarHeight
import com.google.android.gms.location.*
import com.gun0912.tedpermission.rx3.TedPermission
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.*
import com.naver.maps.map.overlay.LocationOverlay
import com.naver.maps.map.overlay.OverlayImage
import com.naver.maps.map.util.FusedLocationSource
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class MapActivity(override val layoutResId: Int = R.layout.activity_map) :
    BaseActivity<ActivityMapBinding>(), MapContract.View, OnMapReadyCallback {
    @Inject
    lateinit var presenter: MapPresenter

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationSource: FusedLocationSource
    private lateinit var mNaverMap: NaverMap
    private lateinit var activityLauncher: ActivityResultLauncher<Intent>

    companion object {
        val TAG = MapActivity::class.simpleName
    }


    override fun initActivity() {
        // 상태바 투명화
        setScreen()
        presenter.onAttach(this)

        setActivityResultLauncher() // 액티비티 런처 설정
        setScrollTopDetection() // 스크롤뷰 스크롤 설정
        setMap() // 맵 관련 함수 설정
        setClickListener() // 클릭 리스너 설정
    }

    private fun setScreen() {
        setStatusBarTransparent()
        // 상태 바, 네비게이션 높이 만큼 padding 주기
        binding.innerContainer.setPadding(0, statusBarHeight(), 0, 0)
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.onDetach()
    }


    private fun setActivityResultLauncher() {
        activityLauncher = registerForActivityResult(
            StartActivityForResult()
        ) { result: ActivityResult ->
            if (result.resultCode == RESULT_OK) {
                val data = result.data
                // RESULT_OK일 때 실행할 코드...
            }
        }
    }

    private fun setClickListener() {
        with(binding) {
            searchVoiceBtn.setOnClickListener {
                activityLauncher.launch(
                    Intent(
                        this@MapActivity,
                        VoiceSearchActivity::class.java
                    )
                )
            }
        }
    }

    // 네이버맵 관련 설정 함수들
    private fun setMap() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        requestPermissions() // 권한 요청
        initMapView() // 네이버 맵 자체 설정 로직
        setUpdateLocationListener()
    }


    private fun setMapUi() {
        setLocator() // 로케이터 UI 설정

        // 로케이션 오버레이 안 보이게 하기 (subIcon 으로 관리하기 때문)
        mNaverMap.locationOverlay.icon = OverlayImage.fromResource(R.drawable.img_transparent)

        with(mNaverMap.uiSettings) {
            isCompassEnabled = false
            isScaleBarEnabled = false
            isZoomControlEnabled = false
            isLocationButtonEnabled = false
        }
    }


    // 맵 로케이터 UI 설정
    private fun setLocator() {
        presenter.currentMapMode.observe(this) { currentMode ->
            when (currentMode) {
                MapMode.INACTIVE -> {
                    Log.d(TAG, "setLocator: Mode1")
                    loadImage(R.drawable.img_inactive_locator, binding.locatorBtn)
                    //mNaverMap.locationTrackingMode = LocationTrackingMode.None
                    mNaverMap.locationOverlay.subIcon =
                        OverlayImage.fromResource(R.drawable.ic_none_mode_overay)
                }
                MapMode.ACTIVE -> {
                    Log.d(TAG, "setLocator: Mode2")
                    loadImage(R.drawable.img_active_locator, binding.locatorBtn)
                    mNaverMap.locationTrackingMode = LocationTrackingMode.NoFollow
                    mNaverMap.locationOverlay.subIcon =
                        OverlayImage.fromResource(R.drawable.ic_no_follow_mode_overay)
                }
                MapMode.DIRECTION_ACTIVE -> {
                    Log.d(TAG, "setLocator: Mode3")
                    val cameraUpdate = CameraUpdate.scrollTo(
                        LatLng(presenter.currentLat, presenter.currentLng)
                    ).animate(CameraAnimation.Linear)
                    mNaverMap.moveCamera(cameraUpdate) // 현재 위치로 이동
                    mNaverMap.locationTrackingMode = LocationTrackingMode.NoFollow

                    loadImage(R.drawable.img_active_arrow_locator, binding.locatorBtn)
                    mNaverMap.locationOverlay.subIcon =
                        OverlayImage.fromResource(R.drawable.ic_follow_mode_overay)
                }
            }
        }

        with(binding) {
            locatorBtn.setOnClickListener {
                // 클릭시 상태전환
                presenter.changeNextMapMode()
            }
        }
    }


    private fun initMapView() {
        // 네이버맵 동적으로 불러오기
        val fm = supportFragmentManager
        val mapFragment = fm.findFragmentById(R.id.map_fragment) as MapFragment?
            ?: MapFragment.newInstance().also {
                fm.beginTransaction().add(R.id.map_fragment, it).commit()
            }

        mapFragment.getMapAsync(this)
    }


    // 최상단으로 스크롤시 구분선을 숨기고
    // 아래로 스크롤시 구분선을 보여줌
    private fun setScrollTopDetection() {
        with(binding) {
            searchScrollView.setOnScrollChangeListener { view, scrollX, scrollY, oldScrollX, oldScrollY ->
                if (!view.canScrollVertically(-1)) {
                    dividerView.visibility = View.GONE
                } else {
                    dividerView.visibility = View.VISIBLE
                }
            }
        }
    }


    // 위치권한 관련 요청
    private fun requestPermissions() {
        // 내장 위치 추적 기능 사용
        locationSource =
            FusedLocationSource(this, Constants.LOCATION_PERMISSION_REQUEST_CODE)

        TedPermission.create()
            .setRationaleTitle("위치권한 요청")
            .setRationaleMessage("현재 위치로 이동하기 위해 위치권한이 필요합니다.") // "we need permission for read contact and find your location"
            .setPermissions(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            .request()
            .subscribe({ tedPermissionResult ->
                if (!tedPermissionResult.isGranted) {
                    showToast("위치 권한 설정을 시도해주세요.")
                }
            }) { throwable -> Log.e(TAG, throwable.message.toString()) }
    }


    // 네이버맵 불러오기가 완료되면 콜백
    @UiThread
    override fun onMapReady(naverMap: NaverMap) {
        this.mNaverMap = naverMap

        setMapUi() // 네이버 맵 UI 설정

        // 내장 위치 추적 기능 사용
        setLocationTracking(mNaverMap)

        // 트래킹 모드 설정
//        setTrackingMode(mNaverMap)

        // 빨간색 표시 마커 (네이버맵 현재 가운데에 항상 위치)
//        val marker = Marker()
//        marker.position = LatLng(
//            naverMap.cameraPosition.target.latitude,
//            naverMap.cameraPosition.target.longitude
//        )
//        marker.icon = OverlayImage.fromResource(R.drawable.ic_favorite)
//        marker.map = naverMap


        // 카메라의 움직임에 대한 이벤트 리스너 인터페이스.
        // 참고 : https://navermaps.github.io/android-map-sdk/reference/com/naver/maps/map/package-summary.html
//        setCameraChangeListener(naverMap)

        // 카메라의 움직임 종료에 대한 이벤트 리스너 인터페이스.
        setCameraIdleListener(mNaverMap)

        // 위치 변경 이벤트
//        setLocationChangeListener(naverMap)

        // 위치 권한을 설정하지 않았다면 이하 로직은 수행하지 않음
        if (!checkLocationPermission()) return

        // 사용자 현재 위치 받아오기
//        var currentLocation: Location?
        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->

            Log.d(TAG, "onMapReady: 사용자 현재 위치")

//            currentLocation = location
//            val currentLat = currentLocation!!.latitude
//            val currentLng = currentLocation!!.longitude
            val currentLat = location!!.latitude
            val currentLng = location.longitude


            // 위치 오버레이의 가시성은 기본적으로 false로 지정되어 있습니다. 가시성을 true로 변경하면 지도에 위치 오버레이가 나타납니다.
            // 파랑색 점, 현재 위치 표시
            mNaverMap.locationOverlay.run {
                isVisible = true
                position = LatLng(currentLat, currentLng)
                circleRadius = 0
                iconWidth = LocationOverlay.SIZE_AUTO
                iconHeight = LocationOverlay.SIZE_AUTO
            }


            // 카메라 현재위치로 이동
            val cameraUpdate = CameraUpdate.scrollTo(
                LatLng(currentLat, currentLng)
            )
            mNaverMap.moveCamera(cameraUpdate)
        }
    }

    private fun setTrackingMode(naverMap: NaverMap) {
        // NoFollow로 설정해야 아이콘이 사라지지 않음.
        naverMap.locationTrackingMode = LocationTrackingMode.NoFollow
    }

    private fun setLocationChangeListener(naverMap: NaverMap) {
        naverMap.addOnLocationChangeListener { location ->
            // 위치 변경시 트래킹 모드 초기화
            presenter.setInitialMapMode()
        }
    }

    // 내 위치 추적 설정
    private fun setLocationTracking(naverMap: NaverMap) {
        naverMap.locationSource = locationSource
    }

    private fun setCameraIdleListener(naverMap: NaverMap) {
        naverMap.addOnCameraIdleListener {
            val lat = naverMap.cameraPosition.target.latitude
            val lng = naverMap.cameraPosition.target.longitude

            // 현재 좌표를 기반으로 주변 주차장 데이터 불러오기
        }
    }

    private fun setCameraChangeListener(naverMap: NaverMap) {
        naverMap.addOnCameraChangeListener { reason, animated ->
            Log.i("NaverMap", "카메라 변경 - reson: $reason, animated: $animated")

            if (presenter.currentMapMode.value == MapMode.DIRECTION_ACTIVE) {
                presenter.setInitialMapMode() // 카메라 이동시 Locator 활성화 끄기
            }
        }
    }

    fun setUpdateLocationListener() {
        val locationRequest = LocationRequest.create()
        locationRequest.run {
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY //높은 정확도
            interval = 1000 //1초에 한번씩 GPS 요청
        }

        val locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                for ((idx, location) in locationResult.locations.withIndex()) {
                    Log.d("location: ", "${location.latitude}, ${location.longitude}")
                    presenter.updateCurrentLocation(location)
                }
            }
        }

        //location 요청 함수 호출 (locationRequest, locationCallback)
        //좌표계를 주기적으로 갱신
        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.myLooper()
        )
    }


    // 좌표 -> 주소 변환
//    private fun getAddress(lat: Double, lng: Double): String {
//        val geoCoder = Geocoder(this, Locale.KOREA)
//        val address: ArrayList<Address>
//        var addressResult = "주소를 가져 올 수 없습니다."
//        try {
//            //세번째 파라미터는 좌표에 대해 주소를 리턴 받는 갯수로
//            //한좌표에 대해 두개이상의 이름이 존재할수있기에 주소배열을 리턴받기 위해 최대갯수 설정
//            address = geoCoder.getFromLocation(lat, lng, 1) as ArrayList<Address>
//            if (address.size > 0) {
//                // 주소 받아오기
//                val currentLocationAddress = address[0].getAddressLine(0)
//                    .toString()
//                addressResult = currentLocationAddress
//
//            }
//
//        } catch (e: IOException) {
//            e.printStackTrace()
//        }
//        return addressResult
//    }

}