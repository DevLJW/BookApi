package com.example.bookapi

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.MotionEvent
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.example.bookapi.Model.BestSellerDto
import com.example.bookapi.Model.Book
import com.example.bookapi.Model.History
import com.example.bookapi.Model.SearchBookDto
import com.example.bookapi.adapter.BookAdapter
import com.example.bookapi.adapter.HistoryAdapter
import com.example.bookapi.databinding.ActivityMainBinding
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.item_book.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.example.bookapi.api.BookService as BookService

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: BookAdapter
    private lateinit var historyadapter: HistoryAdapter
    private lateinit var  bookService: BookService
    private lateinit var  db : Database


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        db = Room.databaseBuilder(applicationContext,Database::class.java,"BookSearchDB").build() //사용할 RoomDB 접근 환경셋팅

        BookView() //도서리스트를 보여주는 리사이클러뷰 어댑터 붙혀놓기
        HistoryView() // //최근검색기록을 보여주는 리사이클러뷰 어댑터 붙혀놓기
        initSearchEditText()







        val retrofit = Retrofit.Builder().baseUrl("https://book.interpark.com").addConverterFactory(GsonConverterFactory.create()).build()
        //List형식의 <Converter.Factory> 제네릭 타입의 변수에  GSon 객체 생성 후 ,빌더 타입으로 반환하여 build() 실행
        


       bookService = retrofit.create(BookService::class.java)


        bookService.getBestSellerBooks(getString(R.string.InterParkapiKey)) //데이터 리퀘스트
            .enqueue(object : Callback<BestSellerDto> {// enque에서 실행

                override fun onResponse(call: Call<BestSellerDto>, response: Response<BestSellerDto>) { //(비동기 콜백)리스폰스 성공시,


                   if(response.isSuccessful.not()){ //issuccessful이 낫일시, 성공한것이 아님
                        return //예외처리
                   }


                    response.body()?.let { //리스폰스 성공시, 리스폰스.바디에 데이터가 들어가 있음(body 리턴) 널이면let 실행X
                        // it -->
                       Log.d(TAG,it.toString())  //최상위 SearchBookDto에 있는 내용 다출력

                        it.books.forEach{book1-> //it : response.body()로 받은 body(베스트셀러DTO데이터)(최상위) BestSellerDto타입
                            Log.d(TAG,book1.toString())


                        }

                       adapter.submitList(it.books)





                    }


                }

                override fun onFailure(call: Call<BestSellerDto>, t: Throwable) { //리스폰스 실패시,

                }




            })







    }



    fun search(keyward : String){

        bookService.getBooksByName(getString(R.string.InterParkapiKey), keyward)
            .enqueue(object : Callback<SearchBookDto> {// enque에서 실행

                override fun onResponse(call: Call<SearchBookDto>, response: Response<SearchBookDto>) { //(비동기 콜백)리스폰스 성공시,
                    hideHistoryView() //검색할때는 리사이클러뷰 화면 가리기
                    saveSearchKeyword(keyward) //검색한 키워드 저장





                    if(response.isSuccessful.not()){ //issuccessful이 낫일시, 성공한것이 아님
                        return //예외처리
                    }


                    response.body()?.let { //리스폰스 성공시, 리스폰스.바디에 데이터가 들어가 있음(body 리턴) 널이면let 실행X
                        // it -->
                        Log.d(TAG,it.toString())  //최상위 SearchBookDto에 있는 내용 다출력

                        it.books.forEach{book1-> //it : response.body()로 받은 body(베스트셀러DTO데이터)(최상위) BestSellerDto타입
                            Log.d(TAG,book1.toString())


                        }

                        adapter.submitList(it.books)





                    }


                }

                override fun onFailure(call: Call<SearchBookDto>, t: Throwable) { //리스폰스 실패시,
                    hideHistoryView()
                }




            })

    }



    fun showHistoryView(){ //DB에서 검색했던 기록들을 가져와 화면에 보여준다.

        Thread{ //쓰레드에서 실행


            val keywords = db.historyDao().getAll().reversed() //DB에서 검색했던 기록을 가져옴
            Log.d(TAG,keywords.toString()) //가져왔는지 로그로 찍어보기


            runOnUiThread { //Runnable 객체를 메인쓰레드에서 실행시키기 위하여 runOnUiThread 구문에서 실행

                binding.historyRecyclerView.isVisible = true //화면에 안보여주던 RecyclerView 레이아웃을 화면에 보여줌
                historyadapter.submitList(keywords.orEmpty()) //어댑터에 데이터 적용하여 데이터를 화면에 보여줌


            }

        }.start()


        binding.historyRecyclerView.isVisible= true

    }

    fun hideHistoryView(){ //화면에서 숨길때

        binding.historyRecyclerView.isVisible= false //리사이클러뷰 화면 숨김
    }





    fun saveSearchKeyword(keyward: String) {  //키워드 DB저장 함수

        Thread {  //Runnable 인스턴스로 만들어지면서 해당 본문을 Run() 메소드로 사용
            
            val alldata = db.historyDao().getAll() //Room 접근 변수(db)dm의 getAll() 함수를 실행시켜 전체 데이터를 받아옴

                alldata.forEach{  //중복검사

                 sametitle ->
                if(sametitle.keyword.toString() == keyward){ //keyward에 해당하는 값이 사용자가 입력한 keyward값이랑 같을때
                    Log.d(TAG,"sameTitle!")

                    return@Thread  //로그 찍고 쓰레드 탈출
                    //추후 시간을 넣어 검색시간만 업데이트하여 최근검색시간으로 위에서부터 정렬예정

                }


            }


            db.historyDao().insertHistory(History(null, keyward)) //DB에 키워드 저장

        }.start()




    }

    fun deleteSearchKeyword(keyward: String){

        Thread{

            db.historyDao().delete(keyward)
            showHistoryView()
        }.start()

    }





    fun BookView(){

        adapter = BookAdapter() //BookAdapter 인스턴스 생성

        binding.bookRecyclerView.layoutManager = LinearLayoutManager(this) //LineyLayout 생성자 실행(액티비티 컨텍스트 리소스에 접근하기위해)
        binding.bookRecyclerView.adapter = adapter //BookAdapter() 인스턴스를 adapter에 등록해놈




    }

    fun HistoryView(){

               historyadapter = HistoryAdapter(historyDeleteClickListener = { //생성자로 람다식 형태로 deleteSearchKeyword 전달
               deleteSearchKeyword(it) //it은 class HistoryAdapter(val historyDeleteClickListener : (String) -> Unit) String을 나타낸다
               })

                binding.historyRecyclerView.layoutManager = LinearLayoutManager(this)//LineyLayout 생성자 실행(액티비티 컨텍스트 리소스에 접근하기위해)
                binding.historyRecyclerView.adapter =  historyadapter //historyAdapter() 인스턴스를 adapter에 등록해놈







    }


    private fun initSearchEditText(){ //검색관련 이벤트 리스너 등록 함수

        //(검색리스너)
        binding.searchText.setOnKeyListener { v, keyCode, event ->  //2

            if(keyCode == KeyEvent.KEYCODE_ENTER && event.action == MotionEvent.ACTION_DOWN){ //터치 이벤트가 엔터로 발생했으면서, 눌렸을때(검색시)

                search(binding.searchText.text.toString()) //검색 텍스트에 입력한 값 serach 함수 파라미터 값으로 전달.


                return@setOnKeyListener true //추후 이벤트를 실행하지 않음

            }





                return@setOnKeyListener false //처리를 못했음을 의미 시스템에서 정의한 다른 이벤트가 처리되야댐(false는 처리하지 못했으니 시스템에서 정의한 다른 이벤트가 처리해야된다고 알려줌)
        }


        //(Edit 터치 리스너)
        binding.searchText.setOnTouchListener { v, event ->  //1


            if(event.action == MotionEvent.ACTION_DOWN){ //누르기만 했을때(터치)

                showHistoryView()  // EditText 클릭시 히스토리 리사이클러뷰를 화면에 표시해줌
                return@setOnTouchListener false // 추후 더 사용할 이벤트들을 계속 처리


            }







            return@setOnTouchListener false//처리하지 못했음을 의미 시스템에서 정의한 다른 이벤트가 처리되어야한다.


        }



/*
    binding.searchText.setOnLongClickListener{v ->

        Toast.makeText(this@MainActivity, "롱클릭", Toast.LENGTH_SHORT).show()
        Log.d(TAG,"롱")

        return@setOnLongClickListener false
    }

        binding.searchText.setOnClickListener{v ->

            Toast.makeText(this@MainActivity, "클릭", Toast.LENGTH_SHORT).show()
            Log.d(TAG,"클릭")
            false
        }
*/


    }






    companion object{
        private  const val  TAG = "MainActvity"
    }




}