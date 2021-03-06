package org.fossasia.openevent.general

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import kotlinx.android.synthetic.main.activity_main.*
import org.fossasia.openevent.general.R.id.*
import org.fossasia.openevent.general.attendees.AttendeeFragment
import org.fossasia.openevent.general.auth.LAUNCH_ATTENDEE
import org.fossasia.openevent.general.auth.ProfileFragment
import org.fossasia.openevent.general.event.EventDetailsFragment
import org.fossasia.openevent.general.event.EventsFragment
import org.fossasia.openevent.general.favorite.FavoriteFragment
import org.fossasia.openevent.general.order.LAUNCH_TICKETS
import org.fossasia.openevent.general.order.OrdersUnderUserFragment
import org.fossasia.openevent.general.order.TICKETS
import org.fossasia.openevent.general.search.SearchFragment
import org.fossasia.openevent.general.utils.Utils.checkAndLoadFragment
import org.fossasia.openevent.general.utils.Utils.loadFragment

private const val TO_SEARCH: String = "ToSearchFragment"

class MainActivity : AppCompatActivity() {

    private val listener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        val fragment: Fragment
        when (item.itemId) {
            R.id.navigation_events -> {
                supportActionBar?.title = "Events"
                fragment = EventsFragment()
                checkAndLoadFragment(supportFragmentManager, fragment, frameContainer.id)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_search -> {
                supportActionBar?.title = "Search"
                fragment = SearchFragment()
                checkAndLoadFragment(supportFragmentManager, fragment, frameContainer.id)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_profile -> {
                supportActionBar?.title = "Profile"
                fragment = ProfileFragment()
                checkAndLoadFragment(supportFragmentManager, fragment, frameContainer.id)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_favorite -> {
                supportActionBar?.title = "Likes"
                fragment = FavoriteFragment()
                checkAndLoadFragment(supportFragmentManager, fragment, frameContainer.id)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_tickets -> {
                supportActionBar?.title = "Tickets"
                fragment = OrdersUnderUserFragment()
                checkAndLoadFragment(supportFragmentManager, fragment, frameContainer.id)
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        navigation.setOnNavigationItemSelectedListener(listener)

        supportActionBar?.title = "Events"

        val bundle = if (savedInstanceState == null) intent.extras else null
        if (bundle != null) {
            if (bundle.getBoolean(TO_SEARCH)) {
                loadFragment(supportFragmentManager, SearchFragment(), frameContainer.id)
                supportActionBar?.title = "Search"
                navigation.selectedItemId = navigation_search
            }

            if (bundle.getBoolean(LAUNCH_ATTENDEE)) {
                val fragment = AttendeeFragment()
                fragment.arguments = bundle
                loadFragment(supportFragmentManager, fragment, frameContainer.id)
            }

            if (bundle.getBoolean(TICKETS) || bundle.getBoolean(LAUNCH_TICKETS)) {
                loadFragment(supportFragmentManager, OrdersUnderUserFragment(), frameContainer.id)
                supportActionBar?.title = "Tickets"
                navigation.selectedItemId = R.id.navigation_tickets
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.profile, menu)
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        menu?.setGroupVisible(R.id.profile_menu, false)
        menu?.setGroupVisible(R.id.search_menu, false)
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onBackPressed() {
        val currentFragment = this.supportFragmentManager.findFragmentById(R.id.frameContainer)
        val rootFragment = this.supportFragmentManager.findFragmentById(R.id.rootLayout)
        if (rootFragment is EventDetailsFragment)
            super.onBackPressed()
        else
            when(currentFragment) {
                is SearchFragment,
                is FavoriteFragment,
                is OrdersUnderUserFragment,
                is ProfileFragment -> {
                    loadFragment(supportFragmentManager, EventsFragment(), frameContainer.id)
                    navigation.selectedItemId = navigation_events
                }
                is EventsFragment -> finish()
                else -> super.onBackPressed()
            }
    }
}
