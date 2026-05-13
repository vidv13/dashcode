package com.vidv13.dashcode.tile

import android.content.Context
import androidx.wear.protolayout.ActionBuilders
import androidx.wear.protolayout.ColorBuilders.argb
import androidx.wear.protolayout.DimensionBuilders
import androidx.wear.protolayout.LayoutElementBuilders
import androidx.wear.protolayout.ModifiersBuilders
import androidx.wear.protolayout.TimelineBuilders
import androidx.wear.tiles.RequestBuilders
import androidx.wear.protolayout.ResourceBuilders
import androidx.wear.tiles.TileBuilders.Tile
import androidx.wear.tiles.TileService
import com.google.common.util.concurrent.ListenableFuture
import com.vidv13.dashcode.MainActivity
import com.vidv13.dashcode.data.QrCodeRepository
import com.vidv13.dashcode.data.local.QrCode
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.guava.future
import javax.inject.Inject

@AndroidEntryPoint
class DashCodeTileService : TileService() {

    @Inject lateinit var repository: QrCodeRepository

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun onDestroy() {
        super.onDestroy()
        scope.cancel()
    }

    override fun onTileRequest(requestParams: RequestBuilders.TileRequest): ListenableFuture<Tile> =
        scope.future {
            val code = repository.getAll().first().firstOrNull()
            buildTile(code)
        }

    override fun onTileResourcesRequest(requestParams: RequestBuilders.ResourcesRequest): ListenableFuture<ResourceBuilders.Resources> =
        scope.future {
            ResourceBuilders.Resources.Builder().setVersion("0").build()
        }

    private fun buildTile(code: QrCode?): Tile {
        val codeName = code?.name ?: "No codes"
        val codeId = code?.id ?: -1L

        val clickable = ModifiersBuilders.Clickable.Builder()
            .setId("open_qr")
            .setOnClick(launchDetailAction(this, codeId))
            .build()

        val layout = LayoutElementBuilders.Box.Builder()
            .setWidth(DimensionBuilders.expand())
            .setHeight(DimensionBuilders.expand())
            .setVerticalAlignment(LayoutElementBuilders.VERTICAL_ALIGN_CENTER)
            .setModifiers(
                ModifiersBuilders.Modifiers.Builder()
                    .setClickable(clickable)
                    .setBackground(
                        ModifiersBuilders.Background.Builder()
                            .setColor(argb(0xFF1B5E20.toInt()))
                            .build(),
                    )
                    .build(),
            )
            .addContent(
                LayoutElementBuilders.Column.Builder()
                    .setHorizontalAlignment(LayoutElementBuilders.HORIZONTAL_ALIGN_CENTER)
                    .addContent(
                        LayoutElementBuilders.Text.Builder()
                            .setText("QR")
                            .setFontStyle(
                                LayoutElementBuilders.FontStyle.Builder()
                                    .setSize(DimensionBuilders.sp(11f))
                                    .setColor(argb(0xFFA5D6A7.toInt()))
                                    .build(),
                            )
                            .build(),
                    )
                    .addContent(
                        LayoutElementBuilders.Text.Builder()
                            .setText(codeName)
                            .setMaxLines(2)
                            .setFontStyle(
                                LayoutElementBuilders.FontStyle.Builder()
                                    .setSize(DimensionBuilders.sp(16f))
                                    .setColor(argb(0xFFFFFFFF.toInt()))
                                    .build(),
                            )
                            .build(),
                    )
                    .build(),
            )
            .build()

        return Tile.Builder()
            .setResourcesVersion("0")
            .setTileTimeline(TimelineBuilders.Timeline.fromLayoutElement(layout))
            .build()
    }

    companion object {
        fun requestUpdate(context: Context) {
            getUpdater(context).requestUpdate(DashCodeTileService::class.java)
        }

        private fun launchDetailAction(context: Context, codeId: Long): ActionBuilders.Action =
            ActionBuilders.LaunchAction.Builder()
                .setAndroidActivity(
                    ActionBuilders.AndroidActivity.Builder()
                        .setClassName(MainActivity::class.java.name)
                        .setPackageName(context.packageName)
                        .addKeyToExtraMapping(
                            MainActivity.EXTRA_CODE_ID,
                            ActionBuilders.AndroidStringExtra.Builder()
                                .setValue(codeId.toString())
                                .build(),
                        )
                        .build(),
                )
                .build()
    }
}
