package danbroid.demo.media2.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction.TRANSIT_FRAGMENT_FADE
import androidx.lifecycle.LifecycleObserver
import com.google.android.material.bottomsheet.BottomSheetBehavior
import danbroid.demo.media2.R

class FadeBetweenFragment : Fragment(R.layout.fade_between_fragment), LifecycleObserver {

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    log.ddebug("onViewCreated()")
    val bottomSheetBehavior = BottomSheetBehavior.from(requireActivity().findViewById(R.id.bottom_controls_fragment))


    // This callback will only be called when MyFragment is at least Started.
    // This callback will only be called when MyFragment is at least Started.
    val callback: OnBackPressedCallback = object : OnBackPressedCallback(false /* enabled by default */) {
      override fun handleOnBackPressed() {
        log.warn("HANDLING THE BACK BUTTON: can go back? ${childFragmentManager.backStackEntryCount}")
        if (childFragmentManager.backStackEntryCount > 0) {
          childFragmentManager.popBackStack()
          bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }
      }
    }

    childFragmentManager.addOnBackStackChangedListener {
      log.ddebug("onBackStackChanged() backCount: ${childFragmentManager.backStackEntryCount}")
      callback.isEnabled = childFragmentManager.backStackEntryCount > 0
    }

    bottomSheetBehavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
      override fun onStateChanged(bottomSheet: View, newState: Int) {
        log.ddebug("onStateChanged() $newState")
        val stateName = when (newState) {
          BottomSheetBehavior.STATE_DRAGGING -> "STATE_DRAGGING"
          BottomSheetBehavior.STATE_SETTLING -> "STATE_SETTLING"
          BottomSheetBehavior.STATE_EXPANDED -> "STATE_EXPANDED"
          BottomSheetBehavior.STATE_COLLAPSED -> "STATE_COLLAPSED"
          BottomSheetBehavior.STATE_HIDDEN -> "STATE_HIDDEN"
          /** The bottom sheet is half-expanded (used when mFitToContents is false).  */

          BottomSheetBehavior.STATE_HALF_EXPANDED -> "STATE_HALF_EXPANDED"
          else -> error("Invalid state: $newState")
        }
        log.ddebug("onStateChanged() $stateName")

        if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
          if (childFragmentManager.backStackEntryCount > 0) {
            childFragmentManager.popBackStack()
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
          }
        } else if (newState == BottomSheetBehavior.STATE_EXPANDED) {
          log.dtrace("loading full controls ..")
          childFragmentManager.beginTransaction().apply {
            replace(R.id.fragment_container, ControlsFragment())
            setTransition(TRANSIT_FRAGMENT_FADE)
            addToBackStack("controls")
            commit()
          }
        }
      }

      override fun onSlide(bottomSheet: View, slideOffset: Float) {
        log.ddebug("onSlide() $slideOffset")
      }

    })



    requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)

    if (childFragmentManager.fragments.isEmpty())
      childFragmentManager.beginTransaction().also {
        it.add(R.id.fragment_container, BottomControlsFragment())
        it.commit()
      }

    view.isFocusableInTouchMode = true

/*    view.findViewById<Button>(R.id.test).setOnClickListener {
      log.dinfo("changing the fragment")
      childFragmentManager.beginTransaction().apply {
        replace(R.id.fragment_container, ControlsFragment())
        setTransition(TRANSIT_FRAGMENT_FADE)
        addToBackStack("Second")
        commit()
      }

      bottomSheet.state = BottomSheetBehavior.STATE_EXPANDED
    }*/


  }


/*
  override fun onActivityCreated(savedInstanceState: Bundle?) {
    log.ddebug("onActivityCreated()")
    super.onActivityCreated(savedInstanceState)


  }*/

}


class TestFragment : Fragment() {
  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? = Button(requireContext()).apply {
    layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
    text = "Hello There"
    setOnClickListener {
      log.derror("clicked me")
    }
  }
}


private val log = danbroid.logging.getLog(FadeBetweenFragment::class)
