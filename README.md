# yizu
fxy
fxy 

#include<iostream>
#define endl '\n'
//#define int long long
#define fir first
#define sec second
// define r(x) scanf("%d",&x)
//#define r1(x) scanf("%lld", &x)
#define ins 0x3f3f3f3f
#define inf 0x3f3f3f3f3f3f3f3f
#define pii pair<int, int>
using namespace std;

const int N = 1e5 + 10;
struct node{
    int l, r;
    int lmax, rmax, smax;
    int lazy;
}tr[N * 4];
int n, m;

void build(int u, int l, int r){
    if(l == r){//到叶子节点，直接更新就好
        tr[u].l = tr[u].r = r;
        tr[u].lmax = tr[u].rmax = tr[u].smax = 1;
        tr[u].lazy = 0;
    }
    else{
        tr[u].l = l, tr[u].r = r;
        tr[u].lmax = tr[u].rmax = tr[u].smax = r - l + 1;
        tr[u].lazy = 0;
        int mid = l + r >> 1;
        build(u << 1, l, mid);
        build(u << 1 | 1, mid + 1, r);
    }
}

void pushup(int u){
    node &uu = tr[u], &ls = tr[u << 1], &rs = tr[u << 1 | 1];
    if(ls.smax == ls.r - ls.l + 1) uu.lmax = ls.smax + rs.lmax;
    else uu.lmax = ls.lmax;
    if(rs.smax == rs.r - rs.l + 1) uu.rmax = rs.smax + ls.rmax;
    else uu.rmax = rs.rmax;
    uu.smax = max(ls.smax, max(rs.smax, ls.rmax + rs.lmax)); 
}

void pushdown(int u){
    node &uu = tr[u], &ls = tr[u << 1], &rs = tr[u << 1 | 1];
    if(uu.lazy){
        if(uu.lazy == 1){
            ls.lazy = rs.lazy = uu.lazy;
            ls.lmax = ls.rmax = ls.smax = 0;
            rs.lmax = rs.rmax = rs.smax = 0;
        }
        else{
            ls.lazy = rs.lazy = uu.lazy;
            ls.lmax = ls.rmax = ls.smax = ls.r - ls.l + 1;
            rs.lmax = rs.rmax = rs.smax = rs.r - rs.l + 1;
        }   
        uu.lazy = 0;
    }
}

void modify(int u, int l, int r, int mark){
    if(l <= tr[u].l && r >= tr[u].r){
        if(mark == 1){
            tr[u].lmax = tr[u].rmax = tr[u].smax = 0;
        }
        else tr[u].lmax = tr[u].rmax = tr[u].smax = tr[u].r - tr[u].l + 1;
        tr[u].lazy = mark;
        return;
    }
    if(tr[u].lazy) pushdown(u);
    int mid = tr[u].l + tr[u].r >> 1;
    if(l <= mid) modify(u << 1, l, r, mark);
    if(r > mid) modify(u << 1 | 1, l, r, mark);
    pushup(u);
}

int query(int u, int len){
    if(tr[u].l == tr[u].r) return tr[u].l;
    if(tr[u].lazy) pushdown(u);
    int mid = tr[u].l + tr[u].r >> 1;
    if(tr[u << 1].smax >= len) return query(u << 1, len);
    else if(tr[u << 1].rmax + tr[u << 1 | 1].lmax >= len) return tr[u << 1].r - tr[u << 1].rmax + 1;
    else return query(u << 1 | 1, len);
}

void solve(){
    cin >> n >> m;
    build(1,1, n);
    while(m --){
        int stt;
        cin >> stt;
        if(stt == 1){
            int x;
            cin >> x;
            if(tr[1].smax >= x){
                int l = query(1, x);
                cout << l << endl;
                modify(1, l, l + x - 1, 1);
            }
            else cout << 0 << endl;
        }
        else{
            int x, y;
            cin >> x >> y;
            modify(1, x, x + y - 1, 2);
        }
    }
}

signed main(){
    ios::sync_with_stdio(false);
    cin.tie(0);
    cout.tie(0);
    solve();
    return 0;
    
一组的仓库
zzm
package com.library.controller;

import com.library.bean.Book;
import com.library.bean.Lend;
import com.library.bean.ReaderCard;
import com.library.service.BookService;
import com.library.service.LendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

@Controller
public class BookController {
    @Autowired
    private BookService bookService;
    @Autowired
    private LendService lendService;

    private Date getDate(String pubstr) {
        try {
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            return df.parse(pubstr);
        } catch (ParseException e) {
            e.printStackTrace();
            return new Date();
        }
    }

    @RequestMapping("/querybook.html")
    public ModelAndView queryBookDo(String searchWord) {
        if (bookService.matchBook(searchWord)) {
            ArrayList<Book> books = bookService.queryBook(searchWord);
            ModelAndView modelAndView = new ModelAndView("admin_books");
            modelAndView.addObject("books", books);
            return modelAndView;
        } else {
            return new ModelAndView("admin_books", "error", "没有匹配的图书");
        }
    }

    @RequestMapping("/reader_querybook_do.html")
    public ModelAndView readerQueryBookDo(String searchWord) {
        if (bookService.matchBook(searchWord)) {
            ArrayList<Book> books = bookService.queryBook(searchWord);
            ModelAndView modelAndView = new ModelAndView("reader_books");
            modelAndView.addObject("books", books);
            return modelAndView;
        } else {
            return new ModelAndView("reader_books", "error", "没有匹配的图书");
        }
    }

    @RequestMapping("/admin_books.html")
    public ModelAndView adminBooks() {
        ArrayList<Book> books = bookService.getAllBooks();
        ModelAndView modelAndView = new ModelAndView("admin_books");
        modelAndView.addObject("books", books);
        return modelAndView;
    }

    @RequestMapping("/book_add.html")
    public ModelAndView addBook() {
        return new ModelAndView("admin_book_add");
    }

    @RequestMapping("/book_add_do.html")
    public String addBookDo(Book book, RedirectAttributes redirectAttributes) {
        //book.setPubdate(getDate(pubstr));
        if (bookService.addBook(book)) {
            redirectAttributes.addFlashAttribute("succ", "图书添加成功！");
        } else {
            redirectAttributes.addFlashAttribute("succ", "图书添加失败！");
        }
        return "redirect:/admin_books.html";
    }

    @RequestMapping("/updatebook.html")
    public ModelAndView bookEdit(HttpServletRequest request) {
        long bookId = Long.parseLong(request.getParameter("bookId"));
        Book book = bookService.getBook(bookId);
        ModelAndView modelAndView = new ModelAndView("admin_book_edit");
        modelAndView.addObject("detail", book);
        return modelAndView;
    }

    @RequestMapping("/book_edit_do.html")
    public String bookEditDo( Book book, RedirectAttributes redirectAttributes) {
        //book.setPubdate(getDate(pubstr));
        if (bookService.editBook(book)) {
            redirectAttributes.addFlashAttribute("succ", "图书修改成功！");
        } else {
            redirectAttributes.addFlashAttribute("error", "图书修改失败！");
        }
        return "redirect:/admin_books.html";
    }

    @RequestMapping("/admin_book_detail.html")
    public ModelAndView adminBookDetail(HttpServletRequest request) {
        long bookId = Long.parseLong(request.getParameter("bookId"));
        Book book = bookService.getBook(bookId);
        ModelAndView modelAndView = new ModelAndView("admin_book_detail");
        modelAndView.addObject("detail", book);
        return modelAndView;
    }

    @RequestMapping("/reader_book_detail.html")
    public ModelAndView readerBookDetail(HttpServletRequest request) {
        long bookId = Long.parseLong(request.getParameter("bookId"));
        Book book = bookService.getBook(bookId);
        ModelAndView modelAndView = new ModelAndView("reader_book_detail");
        modelAndView.addObject("detail", book);
        return modelAndView;
    }

    @RequestMapping("/admin_header.html")
    public ModelAndView admin_header() {
        return new ModelAndView("admin_header");
    }

    @RequestMapping("/reader_header.html")
    public ModelAndView reader_header() {
        return new ModelAndView("reader_header");
    }

    @RequestMapping("/reader_books.html")
    public ModelAndView readerBooks(HttpServletRequest request) {
        ArrayList<Book> books = bookService.getAllBooks();
        ReaderCard readerCard = (ReaderCard) request.getSession().getAttribute("readercard");
        ArrayList<Lend> myAllLendList = lendService.myLendList(readerCard.getReaderId());
        ArrayList<Long> myLendList = new ArrayList<>();
        for (Lend lend : myAllLendList) {
            // 是否已归还 若为null 说明没有归还 将bookid保存在集合中
            if (lend.getBackDate() == null) {
                myLendList.add(lend.getBookId());
            }
        }
        ModelAndView modelAndView = new ModelAndView("reader_books");
        modelAndView.addObject("books", books);
        modelAndView.addObject("myLendList", myLendList);
        return modelAndView;
    }

zjx
#include<bits/stdc++.h>
using namespace std;
int m,v[105];
char a[105][10];
struct node
{
    char str;
    int l=-1,r=-1;
    vector<char> add;
}d[105];
int z(int id,char str)
{
    for(int i=0;i<d[id].add.size();i++)
    {
        if(d[id].add[i]==str)
            return 1;
    }
    return 0;
}
int j(char str)
{
    for(int i=0;i<m;i++)
    {
        if(d[i].str==str||z(i,str))
            return i;
    }
    d[m].str=str;
    return m++;
}
void x(char x,char op,int l,int r)
{
    for(int i=0;i<m;i++)
    {
        if(d[i].str==op&&d[i].l==l&&d[i].r==r)
        {
            d[i].add.push_back(x);
            return;
        }
    }
    d[m].str=op;
    d[m].l=l;
    d[m].r=r;
    d[m].add.push_back(x);
    m++;
    return;
}
char find(int id)
{
    for(int i=0;i<d[id].add.size();i++)
    {
        if(d[id].add[i]=='A'||d[id].add[i]=='B')
            return d[id].add[i];
    }
    return d[id].add[0];
}
void f(int x)
{
    if(d[x].l !=-1)
    {
        v[x]=1;
        f(d[x].l);
        f(d[x].r);
    }
}
void sub2()//专题2
{
	int art[6]={-1},svt[6]={0},lft[6]={0},gnt[30]={0},prc[6][2]={0};
	//art:arrival-time svt:service-time lft:left(进程剩余所需时间) gnt:gannt-chart prc:process
	int p,t,s;
	//p记录下一入队进程,t队尾下标tail(最后一进程后一格),s时间标记
	double sumt=0,sumw=0;
	//用于计算最后两个平均值 
	cout<<"依次输入各进程到达时间,一行五值,空格间隔"<<endl;
	for(int i=1;i<6;i++) cin>>art[i];
	cout<<"依次输入各进程服务时间,一行五值,空格间隔"<<endl;
	for(int i=1;i<6;i++) {cin>>svt[i];lft[i]=svt[i];svt[0]+=svt[i];}//svt静态记录,lft动态统计
	for(p=1,t=s=0;s<svt[0];s++)//无空闲时间片,当前时间小于总服务时间和即可
	{
		if(s==art[p])//如果当前时间s达到下一进程p入队时间art[p]
		{
			gnt[t++]=p;//入队 
			lft[p]--;//剩余量标记--
			p++;
			if(p>5) p=0;//p若>5,全部进程已经入队,s必不为-1,不会再进入此分支,防止越界
		}
		if(lft[gnt[s-1]])
		{
			lft[gnt[s-1]]--;
			gnt[t++]=gnt[s-1];//上一秒s-1执行完毕的进程gnt[s-1]如果还要执行,也入队 
		}
	}
	cout<<endl<<"Gannt Chart:"<<endl<<" |";
	for(int i=0;i<svt[0];i++) cout<<"p"<<gnt[i]<<"|";cout<<endl;
	for(int i=0;i<=svt[0];i++) printf("%2d|",i);cout<<endl;
	t=5;//t记录尚未列好表的进程数量,和下方循环一起产生第二问的表格 
	for(int i=svt[0]-1;i>=0;i--) if(t&&!lft[gnt[i]])//当前进程svt[i]没列到表内 
	{	
		lft[gnt[i]]=1;//此时lft全是0,没用了,当visit用一下做个标记
		prc[t][0]=gnt[i];//记录此进程 
		prc[t][1]=i+1;//记录完成时间
		t--;
	}
	cout<<endl<<"Px| FT | AT | Tr | Ts | Wr "<<endl;
	for(int i=1;i<6;i++)
	{
		int tr=prc[i][1]-art[prc[i][0]];
		double wr=1.0*tr/svt[prc[i][0]];
		sumt+=tr;
		sumw+=wr;
		cout<<"p"<<prc[i][0]<<":";
		printf("%3d |%3d |%3d |%3d | %-5.3f\n",prc[i][1],art[prc[i][0]],tr,svt[prc[i][0]],wr);
	}
	printf("\nT=%.2f\nW=%.2f",sumt/5,sumw/5);//此处保留三位还是两位没测过 
}
void sub3()//专题3 
{
	int rsc[3],avl[3],max[5][3],alc[5][3],nid[5][3],prc[5]={0};//数据存储
	//rsc:resource avl:available max:max alc:allocation nid:need prc:process(记录顺序)
	int vis[5]={0},f=0;//辅助变量
	//vis标记已执行进程,f记录总执行进程数 
	cout<<"输入全部三种资源总量,一行三值,空格间隔"<<endl;
	for(int i=0;i<3;i++)
	{
		cin>>rsc[i];
		avl[i]=rsc[i];
	}
	cout<<"依次输入进程的max和allocation,一行六值,空格间隔"<<endl;
	for(int i=0;i<5;i++)
	{
		for(int j=0;j<3;j++) cin>>max[i][j];
		for(int j=0;j<3;j++) cin>>alc[i][j];
		for(int j=0;j<3;j++) nid[i][j]=max[i][j]-alc[i][j];
		for(int j=0;j<3;j++) avl[j]-=alc[i][j];
	}
	cout<<endl<<"Px|   Max   |Alloca...|   Need  |Available"<<endl;
	for(int i=0;i<5;i++)//输出 
	{
		cout<<"p"<<i<<":";
		for(int j=0;j<3;j++) printf("%3d",max[i][j]);cout<<"|";
		for(int j=0;j<3;j++) printf("%3d",alc[i][j]);cout<<"|";
		for(int j=0;j<3;j++) printf("%3d",nid[i][j]);cout<<"|";
		if(!i) for(int j=0;j<3;j++) printf("%3d",avl[j]);
		cout<<endl;
	}
	cout<<endl<<"Px|   Work  |   Need  |Alloca...|Work+Allo|Finish"<<endl;
	for(int i=0;f!=5;i++,i%=5) if(!vis[i]&&nid[i][0]<=avl[0]&&nid[i][1]<=avl[1]&&nid[i][2]<=avl[2])//进程未执行,且满足执行条件 
	{
		vis[i]=1;
		prc[f++]=i;
		cout<<"p"<<i<<":";
		for(int j=0;j<3;j++) printf("%3d",avl[j]);cout<<"|";
		for(int j=0;j<3;j++) printf("%3d",nid[i][j]);cout<<"|";
		for(int j=0;j<3;j++) printf("%3d",alc[i][j]);cout<<"|";
		for(int j=0;j<3;j++) {avl[j]+=alc[i][j];printf("%3d",avl[j]);}cout<<"|";
		cout<<"true"<<endl;
	}
	cout<<endl<<"safe"<<endl<<endl<<"安全序列:{";
	for(int i=0;i<5;i++)
	{
		cout<<"p"<<prc[i];
		if(i-4) cout<<",";
		else cout<<"}";
	}
}
void sub4_1()//专题4:Clock页面置换算法,默认低物理地址优先 
{
	int frm,nfp,ram[5],flg[5],prl[25],rsl[25],mor[5][25],t,a=0,b=0;//目前最多分配5个物理块,超出会出问题
	//frm:frame(分配物理块数) nfp:next frame pointer(替换指针)
	//ram(当前内存情况) flg:flag(当前访问位情况) t:temp(临时变量) a,b(分别记录PF和PR)
	//prl:page replacement list(页面置换情况)
	//rsl:reference string list(页面访问串)
	//mor:matrix of ram(RAM的页表变化情况)
	for(int i=0;i<5;i++) ram[i]=flg[i]=-1;
	for(int i=0;i<25;i++) prl[i]=rsl[i]=-1;
	for(int i=0;i<5;i++) for(int j=0;j<25;j++) mor[i][j]=-1;//初始化-1,最后只输出非负值即可 
	cout<<"输入所分配的物理块数(最高为5)"<<endl;
	cin>>frm;
	cout<<"输入NF指针初始指向,高地址输入1(正值),低地址输入0"<<endl;
	cin>>nfp;
	if(nfp) nfp=frm-1;
	cout<<"输入已装入内存中的页面号,空格间隔,最后换行"<<endl;//最后添加-1(负值) 
	for(int i=0;cin>>ram[i];i++) if(getchar()=='\n') break;//修改后不需要加-1了 
//	for(int i=0;cin>>t;i++)//t只是为了防止越界,因为开数组的时候卡的极限,不想整大了
//	{
//		if(t<0) break;//输入完毕直接跳出(空置内存块默认-1)
//		ram[i]=t;
//	}
	cout<<"输入内存中页面对应访问位,空格间隔,最后换行"<<endl;//最后添加-1(负值) 
	for(int i=0;cin>>flg[i];i++) if(getchar()=='\n') break;
//	for(int i=0;cin>>t;i++)
//	{
//		if(t<0) break;//输入完毕直接跳出(剩余访问位默认-1) 
//		else if(t) flg[i]=1;//标记访问位
//		else flg[i]=0;
//	}
	cout<<"依次输入页面访问串值,空格间隔,最后换行"<<endl;//最后添加-1(负值) 
	for(int i=0;cin>>rsl[i];i++) if(getchar()=='\n') break;
//	for(int i=0;cin>>t;i++)
//	{
//		if(t<0) {t=i;break;}//t此处记录页面访问串长度,虽然这个值没啥用了,但t变量也没啥用了 
//		rsl[i]=t;
//	}
	for(int i=0;rsl[i]>=0&&i<25;i++)//i此处表页面访问次数(页面访问串下标),i<25是为了防止越界(其实应该把数组开大点的,这太麻烦了) 
	{
		int j;
		for(j=0;j<frm;j++) if(ram[j]==rsl[i])//检查当前ram里是否有要找的页面rsl[i],如果有则 
		{
			flg[j]=1;//访问位置1 
			j=-1;//记录一下有
			break;
		}
		if(j<0) continue;//如果有要找的页面,则不发生缺页中断,直接找下一个访问页(与nfp无关)
		while(1)//后半部分语句均为缺页中断情况,循环查找内存
		{//nfp++%=frm为必带语句,但置换后需要break所以分开写了两次 
			if(flg[nfp]>0)
			{
				flg[nfp]=0;//若访问位为1,则置0
				nfp++;//nfp指针后移 
				nfp%=frm;//nfp指针超出所分配的物理块后会循环指向第一块
			}
			else//否则或者访问位为0进行页面置换,或者分配块为空直接装入 
			{
				a++;//页面中断次数PF增加 
				prl[i]=ram[nfp];//记录:prl的i位置记录被置换出来的页面号,即当前nfp指针指向的ram中的页(如果是空块就是-1赋值给-1,不碍事)
				ram[nfp]=rsl[i];//置换:指针当前所指页面被置换为当前访问页面rsl[i]
				flg[nfp]=1;//置换:新加入页面默认访问位置1; 
				for(j=0;j<frm;j++) mor[j][i]=ram[j];//记录:mor记录当前页面置换后的情况,此处j为ram遍历(0-frm),i为列号 
				nfp++;//nfp指针后移
				nfp%=frm;//nfp指针超出所分配的物理块后会循环指向第一块
				break;
			}
		}
	}
	cout<<endl<<endl<<" PR:|";
	for(int i=0;rsl[i]>=0&&i<25;i++)
	{
		if(prl[i]>=0) {cout<<prl[i]<<"|";b++;}//顺便记录PR 
		else cout<<" |"; 
	}
	cout<<endl<<endl<<" RS:|";
	for(int i=0;rsl[i]>=0&&i<25;i++) cout<<rsl[i]<<"|";
	cout<<endl<<endl<<"RAM:|";
	for(int j=0;j<frm;j++)
	{
		if(j) cout<<"    |";
		for(int i=0;rsl[i]>=0&&i<25;i++)
		{
			if(mor[j][i]>=0) cout<<mor[j][i]<<"|";
			else cout<<" |";
		}
		cout<<endl;
	}
	cout<<endl<<" PF ="<<a<<endl;
	cout<<endl<<" PR ="<<b<<endl;
	cout<<endl<<"Pages to be replaced"<<endl<<"list:";
	t=b;//记一下PR,方便输出列表 
	for(int i=0;rsl[i]>=0&&i<25;i++) if(prl[i]>=0)
	{
		if(t-b) cout<<",";//第一个值前不输出"," 
		t--;
		cout<<prl[i];
	}
	cout<<endl<<endl<<"Page P and access bit A"<<endl<<"list:";
	for(int i=0;i<frm;i++)//有的最后要求不一样 
	{
		if(i) cout<<",";
		cout<<ram[i]<<"-"<<flg[i];
	}
}
void sub4_2()//专题4:Optimal页面置换算法,默认低物理地址优先
{
	int frm,ram[5],flg[5],prl[25],rsl[25],mor[5][25],t,a=0,b=0;//目前最多分配5个物理块,超出会出问题
	//frm:frame(分配物理块数) ram(当前内存情况) flg:flag(标记用)
	//prl:page replacement list(页面置换情况)
	//rsl:reference string list(页面访问串)
	//mor:matrix of ram(RAM的页表变化情况)
	//t:temp(临时变量) a,b(分别记录PF和PR)
	for(int i=0;i<5;i++) ram[i]=flg[i]=-1;//flg没啥用,只把没用的ram空间标记个-1 
	for(int i=0;i<25;i++) prl[i]=rsl[i]=-1;
	for(int i=0;i<5;i++) for(int j=0;j<25;j++) mor[i][j]=-1;//初始化-1,最后只输出非负值即可 
	cout<<"输入所分配的物理块数(最高为5)"<<endl;
	cin>>frm;
	cout<<"输入已装入内存中的页面号,空格间隔,最后换行"<<endl;
	for(int i=0;cin>>ram[i];i++) if(getchar()=='\n') break;
	cout<<"依次输入页面访问串值,空格间隔,最后换行"<<endl;
	for(int i=0;cin>>rsl[i];i++) if(getchar()=='\n') break;
	for(int i=0;rsl[i]>=0&&i<25;i++)//i此处表页面访问次数(页面访问串下标),i<25是为了防止越界(其实应该把数组开大点的,这太麻烦了) 
	{
		int j; 
		for(j=0;j<frm;j++)//每次访问页面,先检查一下内存里有无此页或者空块
		{
			if(ram[j]==rsl[i]) break;//如果有此页面(即使有空块,所以先检查是否有此页)
			else if(ram[j]==-1)//无此页面但有空块
			{
				a++;//没有页面置换b但有缺页中断a  
				ram[j]=rsl[i];//空块应当填入此访问页面 
				for(t=0;t<frm;t++) mor[t][i]=ram[t];//记录:内存变化后情况(剩余空块依旧-1不影响)
				break;//填上之后记录完毕也不用继续查了 
			}
		}
		if(j-frm) continue;
		//如果j!=frm,意味着上方循环是强制跳出,有访问页面或者有空块进行处理,不必进入缺页中断步骤
		//否则一整轮检查完毕后到下方意味着已经没有空块(ram分配块已满)且产生了缺页中断,必定有页面置换
		for(j=0;j<frm;j++)//对于ram中已有的每个页面
		{
			flg[j]=25;//标记初始化,假设访问串后方都无此页面(最大访问顺序下标为24,25找不到) 
			for(t=i+1;rsl[t]>=0&&t<25;t++) if(ram[j]==rsl[t]) //向后(t=i+1)检查访问串,如果找到 
			{
				flg[j]=t;//就记录访问串中最近的此页面的下标t 
				break;//不再找了 
			}
		}//现在ram中每个页面的flg值都是访问串向后最近的此页面的下标
		for(j=t=0;j<frm;j++) if(flg[t]<flg[j]) t=j;//从前向后(默认低地址优先,从0)遍历flg,找到下标最大的页面(等下标时取前者,仅小于更新ram页号t)
		//此时t指向ram中再次调用时位置最远的(或者再也不用的)那些页面中序号最小物理地址最低的页,置换此页
		a++;//缺页中断PF 
		b++;//页面置换PR 
		prl[i]=ram[t];//记录:此次要访问的是串中下标i的页面,被替换出来的页面ram[t]记录在prl的下标i处
		ram[t]=rsl[i];//记录完毕后将ram此页更新为要访问的页面
		for(j=0;j<frm;j++) mor[j][i]=ram[j];//记录:内存变化后情况
	}
	cout<<endl<<endl<<" PR:|";
	for(int i=0;rsl[i]>=0&&i<25;i++)
	{
		if(prl[i]>=0) cout<<prl[i]<<"|";//b++可以顺便记录PR,但之前记了 
		else cout<<" |"; 
	}
	cout<<endl<<endl<<" RS:|";
	for(int i=0;rsl[i]>=0&&i<25;i++) cout<<rsl[i]<<"|";
	cout<<endl<<endl<<"RAM:|";
	for(int j=0;j<frm;j++)
	{
		if(j) cout<<"    |";
		for(int i=0;rsl[i]>=0&&i<25;i++)
		{
			if(mor[j][i]>=0) cout<<mor[j][i]<<"|";
			else cout<<" |";
		}
		cout<<endl;
	}
	cout<<endl<<" PF ="<<a<<endl;
	cout<<endl<<" PR ="<<b<<endl;
	cout<<endl<<"Pages to be replaced"<<endl<<"list:";
	t=b;//记一下PR,方便输出列表
	for(int i=0;rsl[i]>=0&&i<25;i++) if(prl[i]>=0)
	{
		if(t-b) cout<<",";//第一个值前不输出"," 
		t--;
		cout<<prl[i];
	}
}
void sub4_3()//专题4:LRU页面置换算法,默认低物理地址优先(Optimal改的)
{
	int frm,ram[5],flg[5],prl[25],rsl[25],mor[5][25],t,a=0,b=0;//目前最多分配5个物理块,超出会出问题
	//frm:frame(分配物理块数) ram(当前内存情况) flg:flag(标记用)
	//prl:page replacement list(页面置换情况)
	//rsl:reference string list(页面访问串)
	//mor:matrix of ram(RAM的页表变化情况)
	//t:temp(临时变量) a,b(分别记录PF和PR)
	for(int i=0;i<5;i++) ram[i]=flg[i]=-1;//flg没啥用,只把没用的ram空间标记个-1 
	for(int i=0;i<25;i++) prl[i]=rsl[i]=-1;
	for(int i=0;i<5;i++) for(int j=0;j<25;j++) mor[i][j]=-1;//初始化-1,最后只输出非负值即可 
	cout<<"输入所分配的物理块数(最高为5)"<<endl;
	cin>>frm;
	cout<<"依次输入已装入内存中的页面号,空格间隔,最后换行"<<endl;
	for(int i=0;cin>>ram[i];i++) if(getchar()=='\n') break;
	cout<<"依次输入页面访问串值,空格间隔,最后换行"<<endl;
	for(int i=0;cin>>rsl[i];i++) if(getchar()=='\n') break;
	for(int i=0;rsl[i]>=0&&i<25;i++)//i此处表页面访问次数(页面访问串下标),i<25是为了防止越界(其实应该把数组开大点的,这太麻烦了) 
	{
		int j; 
		for(j=0;j<frm;j++)//每次访问页面,先检查一下内存里有无此页或者空块
		{
			if(ram[j]==rsl[i]) break;//如果有此页面(即使有空块,所以先检查是否有此页)
			else if(ram[j]==-1)//无此页面但有空块
			{
				a++;//没有页面置换b但有缺页中断a  
				ram[j]=rsl[i];//空块应当填入此访问页面 
				for(t=0;t<frm;t++) mor[t][i]=ram[t];//记录:内存变化后情况(剩余空块依旧-1不影响)
				break;//填上之后记录完毕也不用继续查了 
			}
		}
		if(j-frm) continue;
		//如果j!=frm,意味着上方循环是强制跳出,有访问页面或者有空块进行处理,不必进入缺页中断步骤
		//否则一整轮检查完毕后到下方意味着已经没有空块(ram分配块已满)且产生了缺页中断,必定有页面置换
		for(j=0;j<frm;j++)//对于ram中已有的每个页面
		{
			flg[j]=-1;//标记初始化,假设访问串前方都无此页面(最小访问顺序下标为0,-1找不到) 
			for(t=i-1;t>=0&&rsl[t]>=0;t--) if(ram[j]==rsl[t]) //向前(t=i-1)检查访问串,如果找到 
			{
				flg[j]=t;//就记录访问串中最近的此页面的下标t 
				break;//不再找了 
			}
		}//现在ram中每个页面的flg值都是访问串向前最近的此页面的下标
		for(j=t=0;j<frm;j++) if(flg[t]>flg[j]) t=j;//从前向后(默认低地址优先,从0)遍历flg,找到下标最小的页面(等下标时取前者,仅大于更新ram页号t)
		//此时t指向ram中再次调用时位置最远的(或者从来没用过的)那些页面中序号最小物理地址最低的页,置换此页
		a++;//缺页中断PF 
		b++;//页面置换PR 
		prl[i]=ram[t];//记录:此次要访问的是串中下标i的页面,被替换出来的页面ram[t]记录在prl的下标i处
		ram[t]=rsl[i];//记录完毕后将ram此页更新为要访问的页面
		for(j=0;j<frm;j++) mor[j][i]=ram[j];//记录:内存变化后情况
	}
	cout<<endl<<endl<<" PR:|";
	for(int i=0;rsl[i]>=0&&i<25;i++)
	{
		if(prl[i]>=0) cout<<prl[i]<<"|";//b++可以顺便记录PR,但之前记了 
		else cout<<" |"; 
	}
	cout<<endl<<endl<<" RS:|";
	for(int i=0;rsl[i]>=0&&i<25;i++) cout<<rsl[i]<<"|";
	cout<<endl<<endl<<"RAM:|";
	for(int j=0;j<frm;j++)
	{
		if(j) cout<<"    |";
		for(int i=0;rsl[i]>=0&&i<25;i++)
		{
			if(mor[j][i]>=0) cout<<mor[j][i]<<"|";
			else cout<<" |";
		}
		cout<<endl;
	}
	cout<<endl<<" PF ="<<a<<endl;
	cout<<endl<<" PR ="<<b<<endl;
	cout<<endl<<"Pages to be replaced"<<endl<<"list:";
	t=b;//记一下PR,方便输出列表
	for(int i=0;rsl[i]>=0&&i<25;i++) if(prl[i]>=0)
	{
		if(t-b) cout<<",";//第一个值前不输出"," 
		t--;
		cout<<prl[i];
	}
}
int main()
{
    int n;
    cin>>n;
    for(int i=0;i<n;i++)
    {
        string s;
        cin>>s;
        int l=j(s[2]);
        int r=j(s[4]);
        x(s[0],s[3],l,r);
    }
    for(int i=0;i<m;i++)
    {
        if(d[i].l !=-1)
        {
        node l=d[d[i].l];
        node r=d[d[i].r];
        a[i][0]=find(i);
        a[i][1]='=';
        a[i][2]=l.add.size()>0?find(d[i].l):l.str;
        a[i][3]=d[i].str;
        a[i][4]=r.add.size()>0?find(d[i].r):r.str;
        a[i][5]='\0';
    }
    }
    for(int i=m-1;i>=0;i--)
    {
        if(a[i][0] =='A')
        {
            f(i);
            break;
        }
    }
    for(int i=m-1;i>=0;i--)
    {
        if(a[i][0]=='B')
        {
            f(i);
            break;
        }
    }
    for(int i=0;i<m;i++)
    {
        if(v[i])
            puts(a[i]);
    }

main
 main
}
