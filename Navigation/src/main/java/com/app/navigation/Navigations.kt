package com.app.navigation

import android.os.Bundle

interface Navigations {
    fun navigate(graph: NavGraph, args: Bundle? = null)
    fun popBackStack()
}