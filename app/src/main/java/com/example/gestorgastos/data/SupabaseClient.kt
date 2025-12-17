package com.example.gestorgastos.data

import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.storage.Storage

object SupabaseClient {
    val client = createSupabaseClient(
        supabaseUrl = "https://ktztcsjmeddhiwsfhxgk.supabase.co",
        supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6Imt0enRjc2ptZWRkaGl3c2ZoeGdrIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NjU5ODE1NjgsImV4cCI6MjA4MTU1NzU2OH0.7UxhaO7i_DJOYudgNze7AStUBjMS-uM2n0UlQ9_onLs"
    ) {
        install(Postgrest)
        install(Storage)
    }
}