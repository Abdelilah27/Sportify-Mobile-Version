package com.app.entity.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.entity.R
import com.app.entity.model.Stadium
import com.app.entity.utils.ConstUtil.GETSTADIUMIMAGE
import com.app.entity.utils.ConstUtil.MAD
import com.app.entity.utils.ConstUtil.PLAYERS
import com.app.entity.utils.OnItemSelectedInterface
import com.bumptech.glide.Glide
import okhttp3.OkHttpClient
import java.security.SecureRandom
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSocketFactory
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

class StadiumsAdapter(val context: Context, private val onItemSelected: OnItemSelectedInterface) :
    RecyclerView.Adapter<StadiumsAdapter.ItemViewHolder>() {

    private var myList: List<Stadium> = listOf()

    @SuppressLint("NotifyDataSetChanged")
    fun setData(data: List<Stadium>) {
        myList = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.stadium_item_stadiums_fragment, parent, false)
        return ItemViewHolder(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        myList[position].let {
            holder.id.text = it.id.toString()
            holder.date.text = it.disponibility_from + "-" + it.disponibility_to
            holder.title.text = it.name
            holder.location.text = it.location
            holder.numberOfPlayer.text = it.numberOfPlayer.toString() + PLAYERS
            holder.price.text = it.price.toString() + MAD
            holder.description.text = it.description
            val stadiumImage = GETSTADIUMIMAGE + it.imgFileName
            Glide.with(context)
                .load(stadiumImage)
                .error(R.drawable.stadium_default)
                .centerCrop()
                .into(holder.image)
        }
    }

    inner class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {
        init {
            view.setOnClickListener(this)
        }

        var id: TextView = view.findViewById(R.id.id)
        var date: TextView = view.findViewById(
            R.id
                .date
        )
        var title: TextView = view.findViewById(
            R.id
                .title
        )
        var location: TextView = view.findViewById(
            R.id
                .location
        )
        var numberOfPlayer: TextView = view.findViewById(
            R.id
                .numberOfPlayer
        )
        var price: TextView = view.findViewById(
            R.id
                .price
        )
        var description: TextView = view.findViewById(
            R.id
                .description
        )
        var image: ImageView = view.findViewById(R.id.image)
        override fun onClick(p0: View?) {
            val position: Int = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                myList[position].let {
                    onItemSelected.onItemClick(it.id)
                }
            }
        }
    }

    override fun getItemCount() = myList.size

    private fun getUnsafeOkHttpClient(): OkHttpClient {
        return try {
            val trustAllCerts: Array<TrustManager> = arrayOf<TrustManager>(
                object : X509TrustManager {
                    @Throws(CertificateException::class)
                    override fun checkClientTrusted(
                        chain: Array<X509Certificate?>?,
                        authType: String?
                    ) {
                    }

                    @Throws(CertificateException::class)
                    override fun checkServerTrusted(
                        chain: Array<X509Certificate?>?,
                        authType: String?
                    ) {
                    }

                    override fun getAcceptedIssuers(): Array<X509Certificate> {
                        return arrayOf()
                    }

                }
            )

            val sslContext: SSLContext = SSLContext.getInstance("SSL")
            sslContext.init(null, trustAllCerts, SecureRandom())

            val sslSocketFactory: SSLSocketFactory = sslContext.socketFactory
            val builder = OkHttpClient.Builder()
            builder.sslSocketFactory(sslSocketFactory, trustAllCerts[0] as X509TrustManager)
            builder.hostnameVerifier { _, _ -> true }
            builder.build()
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }

}