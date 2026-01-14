import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {
    private lateinit var apiService: ApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val etName = findViewById<EditText>(R.id.etName)
        val btnSenddata = findViewById<Button>(R.id.btnSendData)
        val btnFetchData = findViewById<Button>(R.id.btnFetchData)
        val tvResponse = findViewById<TextView>(R.id.tvResponse)


        val retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:3000/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        apiService = retrofit.create(ApiService::class.java)


        btnSenddata.setOnClickListener {
            val name = etName.text.toString().trim()
            if (name.isEmpty()) {
                tvResponse.text = "Nama tidak boleh kosong"
                return@setOnClickListener
            }

            val request = PostRequest(name)
            apiService.sendData(request).enqueue(object : Callback<ApiResponse> {
                override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                    if (response.isSuccessful) {
                        tvResponse.text = response.body()?.message
                        etName.text.clear()
                    } else {
                        tvResponse.text = "Error: ${response.code()}"
                    }
                }

                override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                    tvResponse.text = "Failure: ${t.message}"
                }
            })
        }

        btnFetchData.setOnClickListener {
            apiService.getData().enqueue(object : Callback<ApiResponse> {
                override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                    if (response.isSuccessful) {
                        val dataList = response.body()?.data ?: listOf()
                        val message = response.body()?.message ?: ""
                        tvResponse.text = "$message\nData:\n${dataList.joinToString("\n")}"
                    } else {
                        tvResponse.text = "Error: ${response.code()}"
                    }
                }

                override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                    tvResponse.text = "Failure: ${t.message}"
                }
            })
        }
    }
}
