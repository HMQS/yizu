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
