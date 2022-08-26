package com.app.hackathon.presentation.view.map

import android.Manifest
import com.app.hackathon.presentation.widget.extensions.FlutterExtensions.launchLike
import com.app.hackathon.presentation.widget.extensions.FlutterExtensions.launchParking
import com.app.hackathon.presentation.widget.extensions.FlutterExtensions.setupFlutterNavigation
import android.content.Intent
import android.location.Location
import android.os.Looper
import android.util.Log
import android.view.View
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.annotation.UiThread
import androidx.core.content.ContextCompat
import com.app.hackathon.domain.entity.FilterOption
import com.app.hackathon.domain.entity.LotEntity
import com.app.hackathon.presentation.R
import com.app.hackathon.presentation.base.BaseActivity
import com.app.hackathon.presentation.databinding.ActivityMapBinding
import com.app.hackathon.presentation.presenter.map.MapContract
import com.app.hackathon.presentation.presenter.map.MapPresenter
import com.app.hackathon.presentation.view.report.ReportActivity
import com.app.hackathon.presentation.view.search.TextSearchActivity
import com.app.hackathon.presentation.view.search.VoiceSearchActivity
import com.app.hackathon.presentation.widget.Constants
import com.app.hackathon.presentation.widget.Constants.ADDRESS
import com.app.hackathon.presentation.widget.Constants.LATITUDE
import com.app.hackathon.presentation.widget.Constants.LONGITUDE
import com.app.hackathon.presentation.widget.adapter.FilterOptionAdapter
import com.app.hackathon.presentation.widget.extensions.*
import com.app.hackathon.presentation.widget.utils.PreferenceManager
import com.app.hackathon.presentation.widget.utils.PreferenceManager.Companion.COMPANY_KEY
import com.app.hackathon.presentation.widget.utils.PreferenceManager.Companion.HOME_KEY
import com.bumptech.glide.Glide
import com.google.android.gms.location.*
import com.google.gson.Gson
import com.gun0912.tedpermission.rx3.TedPermission
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.*
import com.naver.maps.map.overlay.LocationOverlay
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.OverlayImage
import com.naver.maps.map.util.FusedLocationSource
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class MapActivity(override val layoutResId: Int = R.layout.activity_map) :
    BaseActivity<ActivityMapBinding>(), MapContract.View, OnMapReadyCallback {
    @Inject
    lateinit var presenter: MapPresenter

    @Inject
    lateinit var preferenceManager: PreferenceManager

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationSource: FusedLocationSource
    private lateinit var mNaverMap: NaverMap
    private lateinit var activityLauncher: ActivityResultLauncher<Intent>
    private lateinit var filterOptionAdapter: FilterOptionAdapter
    private var activeMarkers: ArrayList<Marker> = arrayListOf()

    companion object {
        val TAG = MapActivity::class.simpleName
    }

    override fun onStart() {
        super.onStart()
//        flutterNavigationTo(UI_INITIALIZE) { call, res ->
//
//        }

        binding.editBtn.setOnClickListener {
            setupFlutterNavigation()
            launchLike(
                onLaunchSearchAddress = { address ->
                    activityLauncher.launch(
                        Intent(
                            this@MapActivity,
                            TextSearchActivity::class.java
                        )
                            .putExtra(LATITUDE, presenter.currentLat)
                            .putExtra(LONGITUDE, presenter.currentLng)
                            .putExtra(ADDRESS, address)
                    )
                },
                onUpdateLikeList = { likes ->
                    with(binding) {
                        val homeInfo = likes[0]
                        val companyInfo = likes[1]

                        if (homeInfo.likeName != "집")
                            preferenceManager.setString(HOME_KEY, homeInfo.likeName)
                        if (companyInfo.likeName != "회사")
                            preferenceManager.setString(COMPANY_KEY, companyInfo.likeName)

                        setHomeCompanyName()
                    }
                }
            )
        }
    }

    override fun initActivity() {
        // 상태바 투명화
        setScreen()

        // overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)

        presenter.onAttach(this)

        setActivityResultLauncher() // 액티비티 런처 설정
        setScrollTopDetection() // 스크롤뷰 스크롤 설정
        setFilterOptions() // 필터 리스트 설정
        setMap() // 맵 관련 함수 설정
        setClickListener() // 클릭 리스너 설정
        binding.setHomeCompanyName() // 집, 회사 주소 설정
    }

    private fun setFilterOptions() {
        filterOptionAdapter = FilterOptionAdapter(
            mutableListOf(),
            object : FilterOptionAdapter.OnClickListener {
                override fun onClickItem(item: FilterOption) {
                    // 태그 클릭시 해당 태그 제거
                    presenter.filterOptions.map {
                        if (it.optionName == item.optionName) {
                            it.isChecked *= -1
                        }
                        it
                    }

                    filterOptionAdapter.updateData(presenter.filterOptions)
                }
            })
        binding.searchResultContainer.filterOptionRv.adapter = filterOptionAdapter
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


    private fun ActivityMapBinding.setHomeCompanyName() {
        val homeName = preferenceManager.getString(HOME_KEY) ?: ""
        val companyName = preferenceManager.getString(COMPANY_KEY) ?: ""

        if (homeName.isNotEmpty()) {
            homeLotNameTv.text = homeName
            homeLotNameTv.visibility = View.VISIBLE
            homeLotTitleTv.setTextColor(
                ContextCompat.getColor(
                    this@MapActivity,
                    R.color.primaryTextColor
                )
            )
            homeAddTv.visibility = View.GONE
            homeAddBtn.visibility = View.GONE
            homeIconIv.visibility = View.VISIBLE
        }
        if (companyName.isNotEmpty()) {
            companyLotNameTv.text = companyName
            companyLotNameTv.visibility = View.VISIBLE
            companyLotTitleTv.setTextColor(
                ContextCompat.getColor(
                    this@MapActivity,
                    R.color.primaryTextColor
                )
            )
            companyAddTv.visibility = View.GONE
            companyAddBtn.visibility = View.GONE
            companyIconIv.visibility = View.VISIBLE
        }
    }


    private fun setActivityResultLauncher() {
        activityLauncher = registerForActivityResult(
            StartActivityForResult()
        ) { result: ActivityResult ->
            if (result.resultCode == RESULT_OK) {
                val data = result.data
                // RESULT_OK일 때 실행할 코드...

                val searchResult: LotEntity =
                    data?.getSerializableExtra(Constants.SEARCH_RESULT) as LotEntity
                Log.d(TAG, "setActivityResultLauncher: $searchResult")

                // 현재 보고 있는 주차장 선택
                presenter.selectLot(searchResult)

                showSearchResultContainer()
                updateSearchResult(searchResult.parkName) // 검색 결과를 EditText에 입력
                binding.searchResultContainer.parkName.text = searchResult.parkName // 검색 결과 컨테이너값 변경
                binding.searchResultContainer.searchQueryTv.text = searchResult.parkName
                Glide.with(this@MapActivity).load(provideRandomParkStateImage(searchResult.parkName)).into(binding.searchResultContainer.lotStateIv)
                binding.searchResultContainer.lotLeftCountTv.text = "${searchResult.nowParkCount} / ${searchResult.maxParkCount}"
                binding.searchResultContainer.lotStateTv.text = searchResult.parkState

                // 카메라 해당 위치로 이동
                val cameraUpdate = CameraUpdate.scrollTo(
                    LatLng(searchResult.latitude.toDouble(), searchResult.longitude.toDouble())
                ).animate(CameraAnimation.Fly)
                mNaverMap.moveCamera(cameraUpdate)
            }
        }
    }

    private fun updateSearchResult(searchResult: String) {
        // 검색 결과 텍스트뷰 갱신
        binding.searchResultContainer.searchQueryTv.text = (searchResult)
    }

    private fun showSearchResultContainer() {
        binding.searchResultContainer.root.visibility = View.VISIBLE
        binding.backBtn.visibility = View.VISIBLE
        binding.bottomContainer.visibility = View.GONE
    }

    private fun showSearchContainer() {
        binding.searchResultContainer.root.visibility = View.GONE
        binding.backBtn.visibility = View.GONE
        binding.bottomContainer.visibility = View.VISIBLE
        binding.searchResultContainer.searchQueryTv.text = null
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

            // 텍스트 검색
            searchEditText.setOnClickListener {
                activityLauncher.launch(
                    Intent(
                        this@MapActivity,
                        TextSearchActivity::class.java
                    )
                        .putExtra(LATITUDE, presenter.currentLat)
                        .putExtra(LONGITUDE, presenter.currentLng)
                )
            }

            searchResultContainer.searchVoiceBtn.setOnClickListener {
                activityLauncher.launch(
                    Intent(
                        this@MapActivity,
                        VoiceSearchActivity::class.java
                    )
                )
            }

            searchResultContainer.searchQueryTv.setOnClickListener {
                activityLauncher.launch(
                    Intent(
                        this@MapActivity,
                        TextSearchActivity::class.java
                    )
                        .putExtra(LATITUDE, presenter.currentLat)
                        .putExtra(LONGITUDE, presenter.currentLng)
                        .putExtra("fromVoice", false)
                )
            }

            backBtn.setOnClickListener {
                showSearchContainer()
            }

            searchResultContainer.filterBtn.setOnClickListener {
                FilterBottomSheetDialog.newInstance(
                    presenter.filterOptions,
                    object : FilterBottomSheetDialog.OnDismissListener {
                        override fun onDismiss(filterList: List<FilterOption>) {
                            presenter.updateFilterOptions(filterList)
                            filterOptionAdapter.updateData(filterList)

                            if (presenter.isFiltered()) {
                                presenter.requestFilteredLotsByLocation(
                                    presenter.lookingLat,
                                    presenter.lookingLng
                                )
                            } else {
                                presenter.requestAroundLotsByLocation(
                                    presenter.currentLat,
                                    presenter.currentLng
                                )
                            }
                        }
                    })
                    .show(supportFragmentManager, FilterBottomSheetDialog::class.java.simpleName)
            }

            // 여기 추가
            searchResultContainer.lotDetailBtn.setOnClickListener {
                presenter.selectedLot?.let {
                    setupFlutterNavigation()
                    launchParking(Gson().toJson(it), onLaunchReport = {
                        startActivity(Intent(this@MapActivity, ReportActivity::class.java))
                    })
                }
            }
        }
    }

    // 네이버맵 관련 설정 함수들
    private fun setMap() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        //fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
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
            searchScrollView.setOnScrollChangeListener { view, _, _, _, _ ->
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
        var currentLocation: Location
        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->

            Log.d(TAG, "onMapReady: 사용자 현재 위치")

            if (location != null) {
                Log.d(TAG, "onMapReady: $location")

                currentLocation = location
                val currentLat = currentLocation.latitude
                val currentLng = currentLocation.longitude


                // 위치 오버레이의 가시성은 기본적으로 false로 지정되어 있습니다. 가시성을 true로 변경하면 지도에 위치 오버레이가 나타납니다.
                // 파랑색 점, 현재 위치 표시
                mNaverMap.locationOverlay.run {
                    isVisible = true
                    position = LatLng(currentLat, currentLng)
                    circleRadius = 0
                    iconWidth = LocationOverlay.SIZE_AUTO
                    iconHeight = LocationOverlay.SIZE_AUTO
                }

                presenter.updateCurrentLocation(location)
                moveCamera(currentLat, currentLng)
                // 현재 좌표를 기반으로 주변 주차장 데이터 불러오기
                //presenter.requestFilteredLotsByLocation(currentLat, currentLng)
                if (presenter.isFiltered()) {
                    presenter.requestFilteredLotsByLocation(
                        presenter.lookingLat,
                        presenter.lookingLng
                    )
                }else {
                    presenter.requestAroundLotsByLocation(
                        presenter.currentLat,
                        presenter.currentLng
                    )
                }
            }
        }
    }

    private fun moveCamera(currentLat: Double, currentLng: Double) {
        // 카메라 현재위치로 이동
        val cameraUpdate = CameraUpdate.scrollTo(
            LatLng(currentLat, currentLng)
        )
        mNaverMap.moveCamera(cameraUpdate)
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

            Log.d(TAG, "setCameraIdleListener: naver map Idle")

            // 현재 좌표를 기반으로 주변 주차장 데이터 불러오기
            presenter.updateLookingLocation(lat, lng)

            if (!presenter.isClickMoving) {

                // presenter.requestAroundLotsByLocation(lat, lng)
                //presenter.requestFilteredLotsByLocation(lat, lng)
                if (presenter.isFiltered()) {
                    presenter.requestFilteredLotsByLocation(
                        lat,
                        lng
                    )
                } else {
                    presenter.requestAroundLotsByLocation(
                        lat,
                        lng
                    )
                }
            } else {
                presenter.isClickMoving = false
            }
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

    private fun setUpdateLocationListener() {
        val locationRequest = LocationRequest.create()
        locationRequest.run {
            //priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY //높은 정확도
            interval = 1000 //1초에 한번씩 GPS 요청
        }

        val locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                for ((_, location) in locationResult.locations.withIndex()) {
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

    override fun showLotsOnMap(lotList: List<LotEntity>) {
        // 기존 마커를 모두 제거한다
        freeActiveMarkers()

        Log.d(TAG, "showLotsOnMap: " + lotList.size)
        lotList.forEach { lot ->
            Log.d(TAG, "showLotsOnMap: " + lot)
            makeMarker(lot)
        }
    }

    private fun makeMarker(lot: LotEntity) {
        val marker = Marker().apply {
            position = LatLng(lot.latitude.toDouble(), lot.longitude.toDouble())
            icon =
                OverlayImage.fromResource(provideRandomUnselectedMarkerImage(lot.parkName))
            width = Marker.SIZE_AUTO
            height = Marker.SIZE_AUTO
            map = mNaverMap
            setOnClickListener {
                Log.d(TAG, "makeMarker: ${lot.latitude}")
                presenter.isClickMoving = true

                // 마커 클릭시 해당 위치로 중심점 이동
                val cameraUpdate = CameraUpdate.scrollTo(
                    LatLng(
                        lot.latitude.toDouble(), lot.longitude.toDouble()
                    )
                ).animate(CameraAnimation.Linear)
                mNaverMap.moveCamera(cameraUpdate)

                this.subCaptionText = lot.parkName

                // 기존 마커 색상 초기화
                //resetStateMarkers()
                // 선택한 마커 색상 강조
                icon = OverlayImage.fromResource(provideRandomSelectedMarkerImage(lot.parkName))

                showSearchResultContainer()
                binding.searchResultContainer.searchQueryTv.text = lot.parkName
                binding.searchResultContainer.parkName.text = lot.parkName
                Glide.with(this@MapActivity).load(provideRandomParkStateImage(lot.parkName)).into(binding.searchResultContainer.lotStateIv)
                binding.searchResultContainer.lotLeftCountTv.text = "${lot.nowParkCount} / ${lot.maxParkCount}"
                binding.searchResultContainer.lotStateTv.text = lot.parkState

                presenter.selectLot(lot)

                Log.d(TAG, "makeMarker: " + Gson().toJson(lot))

                return@setOnClickListener false
            }
        }

        activeMarkers.add(marker)
    }


    // 지도상에 표시되고있는 마커들 지도에서 삭제
    private fun freeActiveMarkers() {
        for (activeMarker in activeMarkers) {
            activeMarker.map = null
        }

        activeMarkers.clear()
    }

    private fun isShowSearchResult(): Boolean {
        return binding.searchResultContainer.root.visibility == View.VISIBLE
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


    override fun onBackPressed() {
        if (isShowSearchResult()) {
            showSearchContainer()
        } else {
            super.onBackPressed()
        }
    }
}