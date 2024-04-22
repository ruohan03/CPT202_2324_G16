
package com.example.demo.Controller;

import com.example.demo.Model.Course;
import com.example.demo.Service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import java.util.ArrayList;
import java.util.List;

@Controller
public class CourseController {

    @Autowired
    private CourseService courseService;

//在这个代码中，我们修改了 listCourses 方法，添加了一个 @RequestParam 参数来接收搜索关键词。
// 如果搜索关键词不为空，则调用 courseService.findCoursesByNameContaining(search)方法来获取符合搜索条件的课程列表；
// 否则，调用 courseService.findAllCourses() 方法获取所有课程列表。
    @GetMapping("/courses")
    public String listCourses(@RequestParam(name = "search", required = false) String search, Model model, RedirectAttributes redirectAttributes) {
        List<Course> courses;

        if (search != null && !search.isEmpty()) {
            // 根据课程名称搜索
            List<Course> coursesByName = courseService.findCoursesByNameContaining(search);
            // 根据课程ID搜索
            List<Course> coursesById = courseService.findCoursesByCourseIdContaining(search);

            // 合并两个搜索结果
            courses = new ArrayList<>(coursesByName);
            courses.addAll(coursesById);

            if (courses.isEmpty()) {
                // 如果搜索结果为空，设置一个属性以触发提示消息的显示
                redirectAttributes.addFlashAttribute("noSearchMessage", "No courses found for your search");
                return "redirect:/courses";
            }
        } else {
            // 如果搜索信息不存在，则直接显示全部课程
            courses = courseService.findAllCourses();
        }

        model.addAttribute("courses", courses);
        return "courses";
    }
}
