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
