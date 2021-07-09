package danbroid.demo.media2.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction.TRANSIT_FRAGMENT_FADE
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.sothree.slidinguppanel.SlidingUpPanelLayout
import danbroid.demo.media2.R

class FadeBetweenFragment : Fragment(R.layout.fade_between_fragment), LifecycleObserver {

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    log.ddebug("onViewCreated()")

    val slidingPanel = requireActivity().findViewById<SlidingUpPanelLayout>(R.id.sliding_layout)
    log.warn("GOT SLIDING PANEL $slidingPanel. Current state: ${slidingPanel.panelState}")

    
    if (childFragmentManager.fragments.isEmpty())
      childFragmentManager.beginTransaction().also {
        it.add(R.id.fragment_container, ControlsFragment())
        it.commit()
      }

    view.isFocusableInTouchMode = true

    view.findViewById<Button>(R.id.test).setOnClickListener {
      log.dinfo("changing the fragment")
      childFragmentManager.beginTransaction().apply {
        replace(R.id.fragment_container, TestFragment())
        setTransition(TRANSIT_FRAGMENT_FADE)
        addToBackStack("Second")

        commit()
      }
    }
  }

  @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
  fun onCreated() {
    activity?.lifecycle?.removeObserver(this)

    val slidingPanel = requireActivity().findViewById<SlidingUpPanelLayout>(R.id.sliding_layout)
    log.warn("GOT SLIDING PANEL $slidingPanel. Current state: ${slidingPanel.panelState}")

    slidingPanel.addPanelSlideListener(object : SlidingUpPanelLayout.PanelSlideListener {
      override fun onPanelSlide(panel: View, slideOffset: Float) {
        log.dtrace("onPanelSlide() $slideOffset")
      }

      override fun onPanelStateChanged(panel: View, previousState: SlidingUpPanelLayout.PanelState?, newState: SlidingUpPanelLayout.PanelState?) {
        log.dtrace("onPanelStateChanged() $previousState -> $newState")
      }

    })
  }


  override fun onAttach(context: Context) {
    super.onAttach(context)
    activity?.lifecycle?.addObserver(this)
  }
/*
  override fun onActivityCreated(savedInstanceState: Bundle?) {
    log.ddebug("onActivityCreated()")
    super.onActivityCreated(savedInstanceState)


  }*/

}


class TestFragment : Fragment() {
  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? = Button(requireContext()).apply {
    text = "Hello There"
    setOnClickListener {
      log.derror("clicked me")
    }
  }
}


private val log = danbroid.logging.getLog(FadeBetweenFragment::class)
