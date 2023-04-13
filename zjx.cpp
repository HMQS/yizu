#include<bitsdc++.h>
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
}

