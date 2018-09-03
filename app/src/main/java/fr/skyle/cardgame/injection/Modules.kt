package fr.skyle.cardgame.injection

import com.github.salomonbrys.kodein.Kodein


/**
 * Created by Openium on 20/03/2018.
 */

object Modules {
    val serviceModule = Kodein.Module {
    }

    val eventModule = Kodein.Module {
    }

    val restModule = Kodein.Module {
//        bind<Cache>() with provider {
//            val cacheSize = (20 * 1024 * 1024).toLong() // 20 MiB
//            Cache(instance<Context>().cacheDir, cacheSize)
//        }
//
//        bind<HttpUrl>() with singleton {
//            HttpUrl.parse(instance<Context>().getString(R.string.url_prod))!! // TODO
//        }
//
//        bind<OkHttpClient>() with provider {
//            OkHttpClient.Builder().cache(instance()).build()
//        }
//
//        bind<Retrofit>() with singleton {
//            Retrofit.Builder().baseUrl(instance<HttpUrl>()).client(instance()).addConverterFactory(GsonConverterFactory.create()).addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build()
//        }

//        bind<Api>() with singleton {
//            instance<Retrofit>().create(Api::class.java)
//        }
//
//        bind<OkHttpClient>("logged") with provider {
//            OkHttpClient.Builder().cache(instance())
//                    .addInterceptor(instance())
//                    .build()
//        }
//
//        bind<Gson>() with singleton {
//            GsonBuilder().setLenient().serializeSpecialFloatingPointValues().create()
//        }
//
//        bind<Retrofit>("logged") with singleton {
//            Retrofit.Builder().baseUrl(instance<HttpUrl>()).client(instance("logged")).addConverterFactory(GsonConverterFactory.create(instance())).addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build()
//        }
//
//        bind<ApiLogged>() with singleton {
//            instance<Retrofit>("logged").create(ApiLogged::class.java)
//        }

//        bind<ApiHelper>() with singleton { ApiHelper(instance(), instance(), instance(), instance()) }
    }

//    val utilsModule = Kodein.Module {
//        bind<TokenTester>() with singleton { TokenTesterImpl() }
//    }

}
