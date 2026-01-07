package com.example.my_auth_project

import io.github.jan.supabase.BuildConfig
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest


object SupabaseManager{

    val client = createSupabaseClient(
        supabaseUrl = com.example.my_auth_project.BuildConfig.SUPABASE_URL,
        supabaseKey = com.example.my_auth_project.BuildConfig.SUPABASE_ANON_KEY
    ){
        install(Auth)
        install(Postgrest)

    }

}


