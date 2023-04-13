# yizu
一组的仓库
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
package top.ludonghuang.controller;

import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.ludonghuang.entity.Project;
import top.ludonghuang.entity.Resume;
import top.ludonghuang.service.ProjectService;
import top.ludonghuang.service.ResumeService;
import top.ludonghuang.service.UserDataService;
import top.ludonghuang.utils.Result;
import top.ludonghuang.vo.UserData;

import java.util.Map;

@RestController
@RequestMapping("/project")
public class ProjectController {

    @Autowired
    private ProjectService projectService;
    @Autowired
    private ResumeService resumeService;
    @Autowired
    private UserDataService userDataService;

    @PostMapping("/create")
    public Result create(@RequestBody Project project) {
        int flag = projectService.create(project);
        if (flag > 0) {
            return Result.success();
        } else {
            return Result.error();
        }
    }

    @PostMapping("/delete")
    public Result delete(String ids) {
        int flag = projectService.delete(ids);
        if (flag > 0) {
            return Result.success();
        } else {
            return Result.error();
        }
    }

    @PostMapping("/update")
    public Result update(@RequestBody Project project) {
        int flag = projectService.update(project);
        if (flag > 0) {
            return Result.success();
        } else {
            return Result.error();
        }
    }

    @PostMapping("/detail")
    public Result detail(Integer id) {
        return Result.success(projectService.detail(id));
    }

    @PostMapping("/query")
    public Map<String, Object> query(@RequestBody Project project) {
        UserData user = userDataService.getUser();
        Resume resumeParam = resumeService.detail(user.getId());
        if(resumeParam == null) {
            return Result.success(new PageInfo<>());
        }
        project.setResumeId(resumeParam.getId());
        PageInfo<Project> pageInfo = projectService.query(project);
        pageInfo.getList().forEach(item -> {
            Resume resume = resumeService.detail(item.getResumeId());
            item.setResume(resume);
        });
        return Result.success(pageInfo);
    }

}
public class ResumeController {

    @Autowired
    private ResumeService resumeService;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private UserDataService userDataService;

    @PostMapping("/create")
    public Result create(@RequestBody Resume resume) {
        //获取登录用户的信息
        String token = UserThreadLocal.get();
        UserData userData = (UserData) redisUtil.get(token);
        //设置学生id
        resume.setStudentId(userData.getId());
        int flag = resumeService.create(resume);
        if (flag > 0) {
            return Result.success();
        } else {
            return Result.error();
        }
    }

    @PostMapping("/delete")
    public Result delete(String ids) {
        int flag = resumeService.delete(ids);
        if (flag > 0) {
            return Result.success();
        } else {
            return Result.error();
        }
    }

    @PostMapping("/update")
    public Result update(@RequestBody Resume resume) {
        int flag = resumeService.update(resume);
        if (flag > 0) {
            return Result.success();
        } else {
            return Result.error();
        }
    }

    @PostMapping("/detail")
    public Result detail(Integer id) {
        return Result.success(resumeService.detail(id));
    }

    @PostMapping("/query")
    public Map<String, Object> query(@RequestBody Resume resume) {
        UserData userData = userDataService.getUser();
        resume.setStudentId(userData.getId());
        PageInfo<Resume> pageInfo = resumeService.query(resume);
        return Result.success(pageInfo);
    }

main
}
