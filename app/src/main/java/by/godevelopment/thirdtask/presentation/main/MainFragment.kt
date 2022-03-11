package by.godevelopment.thirdtask.presentation.main

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import by.godevelopment.thirdtask.R
import by.godevelopment.thirdtask.appComponent
import by.godevelopment.thirdtask.databinding.FragmentMainBinding
import by.godevelopment.thirdtask.di.factory.ViewModelFactory
import by.godevelopment.thirdtask.presentation.list.ListFragment
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    @Inject
    lateinit var viewModelFactor: ViewModelFactory
    lateinit var viewModel: MainViewModel

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding !!

    override fun onAttach(context: Context) {
        context.appComponent.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this, viewModelFactor)[MainViewModel::class.java]
        setupUI()
        setupListeners()
        setupEvent()
        return binding.root
    }

    private fun setupUI() {

    }

    private fun setupListeners() {
        binding.btnSelect.setOnClickListener {
            navigateToList()
        }
    }

    private fun setupEvent() {
        lifecycleScope.launchWhenStarted {
            viewModel.eventUI.collect {
                Snackbar
                    .make(
                        binding.root,
                        it.message,
                        Snackbar.LENGTH_LONG
                    )
                    .show()
            }
        }
    }

    private fun navigateToList() {
        parentFragmentManager.commit {
            setReorderingAllowed(true)
            replace<ListFragment>(R.id.fragment_container_view)
            addToBackStack(null)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}