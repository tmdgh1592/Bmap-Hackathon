package com.app.hackathon.presentation.view.map

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.FrameLayout
import androidx.core.os.bundleOf
import com.app.hackathon.domain.entity.FilterOption
import com.app.hackathon.presentation.R
import com.app.hackathon.presentation.databinding.FragmentFilterBottomSheetDialogBinding
import com.app.hackathon.presentation.widget.Constants.FILTER_OPTIONS
import com.app.hackathon.presentation.widget.Constants.FILTER_OPTIONS_BUNDLE
import com.app.hackathon.presentation.widget.extensions.getWindowHeight
import com.app.hackathon.presentation.widget.extensions.toBoolean
import com.app.hackathon.presentation.widget.extensions.toInt
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlin.math.roundToInt


class FilterBottomSheetDialog : BottomSheetDialogFragment() {

    lateinit var onDismissListener: OnDismissListener
    private var _binding: FragmentFilterBottomSheetDialogBinding? = null
    private val binding get() = _binding!!
    private val filterOptions = mutableListOf<FilterOption>()


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val bottomSheetDialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog

        bottomSheetDialog.setOnShowListener {
            val bottomSheet = bottomSheetDialog
                .findViewById<FrameLayout>(com.google.android.material.R.id.design_bottom_sheet)

            if (bottomSheet != null) {
                val behavior: BottomSheetBehavior<*> = BottomSheetBehavior.from(bottomSheet)
                behavior.isDraggable = false
            }
        }

        return bottomSheetDialog
    }

    override fun getTheme(): Int {
        return R.style.CustomBottomSheetDialog
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // 이미 설정된 필터 세팅
        filterOptions.addAll(
            arguments?.getBundle(FILTER_OPTIONS_BUNDLE)?.getParcelableArrayList(FILTER_OPTIONS)!!
        )

        _binding = FragmentFilterBottomSheetDialogBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setTagState()
        setTagClickListener()
        setDoneClickListener()
    }

    private fun setTagState() {
        with(binding) {
            passageFirstOption.isChecked = filterOptions[0].isChecked.toBoolean()
            passageSecondOption.isChecked = filterOptions[1].isChecked.toBoolean()
            passageThirdOption.isChecked = filterOptions[2].isChecked.toBoolean()
            passageFourthOption.isChecked = filterOptions[3].isChecked.toBoolean()
            passageFifthOption.isChecked = filterOptions[4].isChecked.toBoolean()
            looksFirstOption.isChecked = filterOptions[5].isChecked.toBoolean()
            looksSecondOption.isChecked = filterOptions[6].isChecked.toBoolean()
            convenienceFirstOption.isChecked = filterOptions[7].isChecked.toBoolean()
            convenienceSecondOption.isChecked = filterOptions[8].isChecked.toBoolean()
        }
    }

    private fun setDoneClickListener() {
        binding.doneBtn.setOnClickListener {
            dismissAllowingStateLoss()
        }
    }

    private fun setTagClickListener() {
        with(binding) {
            passageFirstOption.setOnCheckedChangeListener { _, isChecked ->
                filterOptions[0].isChecked = isChecked.toInt()
            }
            passageSecondOption.setOnCheckedChangeListener { _, isChecked ->
                filterOptions[1].isChecked = isChecked.toInt()
            }
            passageThirdOption.setOnCheckedChangeListener { _, isChecked ->
                filterOptions[2].isChecked = isChecked.toInt()
            }
            passageFourthOption.setOnCheckedChangeListener { _, isChecked ->
                filterOptions[3].isChecked = isChecked.toInt()
            }
            passageFifthOption.setOnCheckedChangeListener { _, isChecked ->
                filterOptions[4].isChecked = isChecked.toInt()
            }
            looksFirstOption.setOnCheckedChangeListener { _, isChecked ->
                filterOptions[5].isChecked = isChecked.toInt()
            }
            looksSecondOption.setOnCheckedChangeListener { _, isChecked ->
                filterOptions[6].isChecked = isChecked.toInt()
            }
            convenienceFirstOption.setOnCheckedChangeListener { _, isChecked ->
                filterOptions[7].isChecked = isChecked.toInt()
            }
            convenienceSecondOption.setOnCheckedChangeListener { _, isChecked ->
                filterOptions[8].isChecked = isChecked.toInt()
            }
        }
    }

    companion object {

        fun newInstance(
            filterOptions: List<FilterOption>,
            onDismissListener: OnDismissListener
        ): FilterBottomSheetDialog {

            return FilterBottomSheetDialog().apply {
                arguments = Bundle().apply {
                    putBundle(FILTER_OPTIONS_BUNDLE, bundleOf(FILTER_OPTIONS to filterOptions))
                }
                this.onDismissListener = onDismissListener
                Log.d("TAG", "newInstance: dddd")
            }
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        onDismissListener.onDismiss(filterOptions)
    }

    interface OnDismissListener {
        fun onDismiss(filterList: List<FilterOption>)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}