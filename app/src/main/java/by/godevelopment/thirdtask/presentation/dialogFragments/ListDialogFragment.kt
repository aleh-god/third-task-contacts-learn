package by.godevelopment.thirdtask.presentation.dialogFragments

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentResultListener
import androidx.lifecycle.LifecycleOwner
import by.godevelopment.thirdtask.R

class ListDialogFragment : DialogFragment() {

    private val list: Array<String> by lazy {
        val arrayList = requireArguments().getStringArrayList(ARG_LIST) as ArrayList<String>
        arrayList.toTypedArray()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(requireContext())
            .setTitle(R.string.contact_choice)
            .setNeutralButton(R.string.action_ignore, null)
            .setItems(list, DialogInterface.OnClickListener { _, which ->
                val item = list[which]
                Log.i(TAG, "onCreateDialog: which = $which, item = $item")
                parentFragmentManager.setFragmentResult(REQUEST_KEY, bundleOf(KEY_LIST_RESPONSE to which))
            })
            .create()
    }

    companion object {
        @JvmStatic private val TAG = ListDialogFragment::class.java.simpleName
        @JvmStatic private val KEY_LIST_RESPONSE = "KEY_LIST_RESPONSE"
        @JvmStatic private val ARG_LIST = "ARG_LIST"

        @JvmStatic val REQUEST_KEY = "$TAG:defaultRequestKey"

        fun show (manager: FragmentManager, list: ArrayList<String>) {
            val listDialogFragment = ListDialogFragment()
            listDialogFragment.arguments = bundleOf(ARG_LIST to list)
            listDialogFragment.show(manager, TAG)
        }

        fun setupListener(manager: FragmentManager, lifecycleOwner: LifecycleOwner, listener: (Int) -> Unit) {
            manager.setFragmentResultListener(REQUEST_KEY, lifecycleOwner, FragmentResultListener { _, result ->
                listener.invoke(result.getInt(KEY_LIST_RESPONSE))
            })
        }
    }
}