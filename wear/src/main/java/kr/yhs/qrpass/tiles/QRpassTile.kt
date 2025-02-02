package kr.yhs.qrpass.tiles

import androidx.core.content.ContextCompat
import androidx.wear.tiles.*
import androidx.wear.tiles.ColorBuilders.argb
import androidx.wear.tiles.DeviceParametersBuilders.DeviceParameters
import androidx.wear.tiles.DimensionBuilders.*
import androidx.wear.tiles.LayoutElementBuilders.*
import androidx.wear.tiles.ModifiersBuilders.Clickable
import androidx.wear.tiles.ModifiersBuilders.Modifiers
import androidx.wear.tiles.ResourceBuilders.Resources
import androidx.wear.tiles.TimelineBuilders.Timeline
import androidx.wear.tiles.TimelineBuilders.TimelineEntry
import com.google.common.util.concurrent.ListenableFuture
import kotlinx.coroutines.*
import kotlinx.coroutines.guava.future
import kr.yhs.qrpass.MainActivity
import kr.yhs.qrpass.R
import kr.yhs.qrpass.tiles.LayoutBuilder.font
import kr.yhs.qrpass.tiles.LayoutBuilder.text
import kr.yhs.qrpass.tiles.TilesBuilder.background
import kr.yhs.qrpass.tiles.TilesBuilder.corner
import kr.yhs.qrpass.tiles.TilesBuilder.padding


class QRpassTile : TileService() {
    private val RESOURCES_VERSION = "1"
    private val serviceScope = CoroutineScope(Dispatchers.IO)

    private lateinit var deviceParameters: DeviceParameters

    override fun onTileRequest(requestParams: RequestBuilders.TileRequest): ListenableFuture<TileBuilders.Tile> =
        serviceScope.future {
            deviceParameters = requestParams.deviceParameters!!
            TileBuilders.Tile.Builder()
                .setResourcesVersion(RESOURCES_VERSION)
                .setFreshnessIntervalMillis(3600000) // 60 minutes
                .setTimeline(
                    Timeline.Builder().addTimelineEntry(
                        TimelineEntry.Builder().setLayout(
                            Layout.Builder().setRoot(
                                getLayout()
                            ).build()
                        ).build()
                    ).build()
                ).build()
        }

    override fun onResourcesRequest(requestParams: RequestBuilders.ResourcesRequest): ListenableFuture<Resources> =
        serviceScope.future {
            Resources.Builder()
                .setVersion(RESOURCES_VERSION)
                .addIdToImageMapping(
                    TileButtonResourceId,
                    ResourceBuilders.ImageResource.Builder().apply {
                        setAndroidResourceByResId(
                            ResourceBuilders.AndroidImageResourceByResId.Builder()
                                .setResourceId(R.drawable.logo_background)
                                .build()
                        )
                    }.build()
                )
                .build()
        }

    override fun onDestroy() {
        super.onDestroy()
        serviceScope.cancel()
    }

    private fun getLayout() =
        Box.Builder().apply {
            setWidth(expand())
            setHeight(expand())

            addContent(
                getColumn(
                    "QRpass",
                    "아래의 버튼을 눌러 QR 체크인을 하세요."
                )
            )
        }.build()

    private fun getColumn(data1: String, data2: String) =
        Column.Builder().apply {
            addContent(
                text(
                    textString = data1,
                    fontStyles = font(
                        size = sp(16f),
                        widght = FONT_WEIGHT_NORMAL
                    )
                )
            )
            addContent(
                text(
                    textString = data2,
                    fontStyles = font(
                        size = sp(13f)
                    )
                )
            )
            addContent(
                getSpacer(
                    height = dp(30f)
                )
            )
            addContent(
                getButton(
                    R.color.white,
                    resourceId = TileButtonResourceId,
                    clickable = Clickable.Builder().apply {
                        setOnClick(
                            ActionBuilders.LaunchAction.Builder().apply {
                                setId("Trigger-QRpass-imageTask")
                                setAndroidActivity(
                                    ActionBuilders.AndroidActivity.Builder().apply {
                                        setClassName(MainActivity::class.java.name)
                                        setPackageName("kr.yhs.qrpass")
                                    }.build()
                                )
                            }.build()
                        )
                    }.build()
                )
            )
        }.build()

    private fun getSpacer(
        width: DpProp? = null,
        height: DpProp? = null
    ) =
        Spacer.Builder().apply {
            if (width != null)
                setWidth(width)
            if (height != null)
                setHeight(height)
        }.build()

    private fun getButton(
        color: Int,
        resourceId: String,
        clickable: Clickable? = null,
        paddingSize: DpProp = dp(12f),
        buttonSize: DpProp = dp(48f)
    ) =
        Image.Builder().apply {
            setResourceId(resourceId)
            setHeight(buttonSize)
            setWidth(buttonSize)
            setContentScaleMode(CONTENT_SCALE_MODE_FILL_BOUNDS)
            setModifiers(
                Modifiers.Builder().apply {
                    setPadding(
                        padding(all = paddingSize)
                    )
                    setBackground(
                        background(
                            corner = corner(buttonSize),
                            color = argb(
                                ContextCompat.getColor(
                                    this@QRpassTile, color
                                )
                            )
                        )
                    )
                    if (clickable != null)
                        setClickable(clickable)
                }.build()
            )
        }.build()

    companion object {
        private const val TileButtonResourceId = "tile_button_id"
    }
}