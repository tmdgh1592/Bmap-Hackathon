package com.app.hackathon.presentation.widget.extensions

import android.content.Context
import android.content.Intent
import com.google.gson.Gson
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.embedding.engine.FlutterEngineCache
import io.flutter.embedding.engine.dart.DartExecutor
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

object FlutterExtensions {
    /**
     *
     * ROUTES
     *
     */
    private const val ROUTE_INIT = "/"
    private const val ROUTE_LIKE = "/like";
    private const val ROUTE_PARKING = "/parking";
    private lateinit var searchAddressResult: MethodChannel.Result

    /**
     *
     * DATA CLASS
     *
     */
    data class LikeItem(
        val type: String, val likeName: String, val likeAddress: String
    )

    /**
     *
     * Navigator Extensions
     *
     */

// Initialize
    fun Context.setupFlutterNavigation() {
        setupNavigation(ROUTE_LIKE)
        setupNavigation(ROUTE_PARKING)
    }


    fun launchLikeFinished(address: String) {
        searchAddressResult.success(mapOf("address" to address))
    }


    fun Context.launchLike(
        onLaunchSearchAddress: (String) -> Unit,
        onUpdateLikeList: (List<LikeItem>) -> Unit
    ) {
        flutterNavigationTo(ROUTE_LIKE) { call, res ->
            when (call.method) {
                // 주소 검색
                "searchAddress" -> {
                    val arguments = call.arguments as Map<*, *>
                    searchAddressResult = res
                    onLaunchSearchAddress(arguments["address"]?.toString() ?: "")
                }
                // 즐겨찾기 목록 전달
                "saveAddress" -> {
                    val arguments = call.arguments as List<*>
                    val args =
                        arguments.map { Gson().fromJson(it.toString(), LikeItem::class.java) }
                    onUpdateLikeList(args)
                    res.success(true)
                }
            }
        }
    }

    fun Context.launchParking(data: String, onLaunchReport: () -> Unit) {
        val rawData: String = data
        flutterNavigationTo(ROUTE_PARKING) { call, res ->
            when (call.method) {
                "loadData" -> {
                    res.success(rawData);
                }
                "launchReport" -> {
                    onLaunchReport()
                    res.success(true);
                }
            }
        }
    }

    private fun Context.setupNavigation(path: String) {
        val flutterEngine = FlutterEngine(this)
        flutterEngine.navigationChannel.setInitialRoute(path)
        flutterEngine.dartExecutor.executeDartEntrypoint(DartExecutor.DartEntrypoint.createDefault())
        FlutterEngineCache.getInstance().put(path, flutterEngine)
    }

    private fun Context.flutterNavigationTo(
        path: String,
        callback: suspend (MethodCall, MethodChannel.Result) -> Unit
    ) {
        val flutterEngine =
            FlutterEngineCache.getInstance().get(path)
                ?: throw java.lang.NullPointerException("Flutter 초기화가 수행되지 않았습니다")
        MethodChannel(flutterEngine.dartExecutor, path).setMethodCallHandler { call, res ->
            CoroutineScope(Dispatchers.Main).launch {
                callback.invoke(call, res)
            }
        }
        startActivity(FlutterActivity.withCachedEngine(path).build(this).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        })
    }
}