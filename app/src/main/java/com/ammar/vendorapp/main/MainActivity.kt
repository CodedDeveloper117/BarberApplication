package com.ammar.vendorapp.mainimport android.os.Bundleimport android.view.WindowManagerimport androidx.appcompat.app.AppCompatActivityimport androidx.navigation.NavControllerimport androidx.navigation.NavGraphimport androidx.navigation.fragment.NavHostFragmentimport com.ammar.vendorapp.Rimport com.ammar.vendorapp.authentication.data.repositories.DatastoreRepositoryimport com.ammar.vendorapp.databinding.ActivityAuthenticationBindingimport com.ammar.vendorapp.databinding.ActivityMainBindingimport dagger.hilt.android.AndroidEntryPointimport javax.inject.Inject@AndroidEntryPointclass MainActivity : AppCompatActivity() {    private lateinit var binding: ActivityMainBinding    lateinit var navController: NavController    lateinit var navHostFragment: NavHostFragment    private lateinit var navGraph: NavGraph    override fun onCreate(savedInstanceState: Bundle?) {        super.onCreate(savedInstanceState)        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)        binding = ActivityMainBinding.inflate(layoutInflater)        setContentView(binding.root)        navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment        navController = navHostFragment.navController        navGraph = navController.navInflater.inflate(R.navigation.main_graph)        //val isFromMainActivity = intent.getBooleanExtra("is_from_main_activity", false)        navGraph.setStartDestination(R.id.launchFragment)        navController.graph = navGraph    }}/*-Xms1024m-Xmx4096m-XX:MaxPermSize=1024m-XX:ReservedCodeCacheSize=440m-XX:+UseCompressedOops-XX:-HeapDumpOnOutOfMemoryError-Dfile.encoding=UTF-8*/