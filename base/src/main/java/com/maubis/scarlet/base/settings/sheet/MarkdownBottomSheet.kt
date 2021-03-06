package com.maubis.scarlet.base.settings.sheet

import android.app.Dialog
import android.widget.TextView
import com.github.bijoysingh.uibasics.views.UIActionView
import com.maubis.scarlet.base.R
import com.maubis.scarlet.base.config.CoreConfig
import com.maubis.scarlet.base.support.ui.ThemeColorType
import com.maubis.scarlet.base.support.ui.ThemedActivity
import com.maubis.scarlet.base.support.ui.ThemedBottomSheetFragment
import ru.noties.markwon.Markwon

class MarkdownBottomSheet : ThemedBottomSheetFragment() {
  override fun getBackgroundView(): Int = R.id.container_layout

  override fun setupView(dialog: Dialog?) {
    super.setupView(dialog)
    if (dialog == null) {
      return
    }

    setupDialogContent(dialog)
    val sourceText = dialog.findViewById<TextView>(R.id.source_text);
    val markdownText = dialog.findViewById<TextView>(R.id.markdown_text);
    sourceText.setText(R.string.markdown_sheet_examples_list)
    markdownText.setText(Markwon.markdown(themedContext(), getString(R.string.markdown_sheet_examples_list)))

    val exampleTitle = dialog.findViewById<TextView>(R.id.examples_title)
    exampleTitle.setTextColor(CoreConfig.instance.themeController().get(ThemeColorType.TERTIARY_TEXT))

    sourceText.setTextColor(CoreConfig.instance.themeController().get(ThemeColorType.SECONDARY_TEXT))
    markdownText.setTextColor(CoreConfig.instance.themeController().get(ThemeColorType.SECONDARY_TEXT))
    makeBackgroundTransparent(dialog, R.id.root_layout)
  }

  override fun getBackgroundCardViewIds(): Array<Int> = arrayOf(
      R.id.markdown_card, R.id.markdown_example_card)

  fun setupDialogContent(dialog: Dialog) {
    val isMarkdownEnabled = isMarkdownEnabled()
    val actionButton = dialog.findViewById<UIActionView>(R.id.action_button)
    actionButton.setOnClickListener {
      CoreConfig.instance.store().put(SettingsOptionsBottomSheet.KEY_MARKDOWN_ENABLED, !isMarkdownEnabled)
      dismiss()
    }
    actionButton.setTitleColor(getOptionsTitleColor(isMarkdownEnabled))
    actionButton.setSubtitleColor(getOptionsSubtitleColor(isMarkdownEnabled))
    actionButton.setImageTint(getOptionsTitleColor(isMarkdownEnabled))
    if (isMarkdownEnabled) {
      actionButton.setActionResource(R.drawable.ic_check_box_white_24dp);
    }

    val isMarkdownHomeEnabled = CoreConfig.instance.store().get(SettingsOptionsBottomSheet.KEY_MARKDOWN_HOME_ENABLED, true)
    val markdownHomeButton = dialog.findViewById<UIActionView>(R.id.markdown_home_button)
    if (isMarkdownEnabled) {
      markdownHomeButton.setOnClickListener {
        CoreConfig.instance.store().put(SettingsOptionsBottomSheet.KEY_MARKDOWN_HOME_ENABLED, !isMarkdownHomeEnabled)
        setupDialogContent(dialog)
      }
    }
    markdownHomeButton.setTitleColor(getOptionsTitleColor(isMarkdownEnabled && isMarkdownHomeEnabled))
    markdownHomeButton.setSubtitleColor(getOptionsSubtitleColor(isMarkdownEnabled && isMarkdownHomeEnabled))
    markdownHomeButton.setImageTint(getOptionsTitleColor(isMarkdownEnabled && isMarkdownHomeEnabled))
    if (isMarkdownEnabled && isMarkdownHomeEnabled) {
      markdownHomeButton.setActionResource(R.drawable.ic_check_box_white_24dp);
    } else {
      markdownHomeButton.setActionResource(0);
    }

  }

  override fun getLayout(): Int = R.layout.bottom_sheet_markdown

  companion object {
    fun openSheet(activity: ThemedActivity) {
      val sheet = MarkdownBottomSheet()

      sheet.show(activity.supportFragmentManager, sheet.tag)
    }

    fun isMarkdownEnabled() = CoreConfig.instance.store()
        .get(SettingsOptionsBottomSheet.KEY_MARKDOWN_ENABLED, true)

    fun isMarkdownHomeEnabled() = CoreConfig.instance.store()
        .get(SettingsOptionsBottomSheet.KEY_MARKDOWN_HOME_ENABLED, true)

  }
}