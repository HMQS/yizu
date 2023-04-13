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

}