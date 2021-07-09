package danbroid.demo.media2.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import danbroid.demo.media2.databinding.FragmentControlsBinding

class ControlsFragment : Fragment() {

  private var _binding: FragmentControlsBinding? = null
  val binding: FragmentControlsBinding
    get() = _binding!!


  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    binding.buttonBack.setOnClickListener {
      log.debug("buttonBack clicked")
      parentFragment?.childFragmentManager?.popBackStack()
    }
  }


  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) = FragmentControlsBinding.inflate(inflater, container, false).let {
    _binding = it
    it.root
  }

  override fun onDestroyView() {
    super.onDestroyView()
    _binding = null
  }

}

private val log = danbroid.logging.getLog(ControlsFragment::class)
