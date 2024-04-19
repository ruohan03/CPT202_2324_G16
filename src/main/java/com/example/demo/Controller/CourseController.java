
package com.example.demo.Controller;

import com.example.demo.Model.Course;
import com.example.demo.Model.Trainer;
import com.example.demo.Service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;


import java.util.List;

@Controller
public class CourseController {

    @Autowired
    private CourseService courseService;

//在这个代码中，我们修改了 listCourses 方法，添加了一个 @RequestParam 参数来接收搜索关键词。
// 如果搜索关键词不为空，则调用 courseService.findCoursesByNameContaining(search)方法来获取符合搜索条件的课程列表；
// 否则，调用 courseService.findAllCourses() 方法获取所有课程列表。
    @GetMapping("/courses")
    public String listCourses(@RequestParam(name = "search", required = false) String search, Model model) {
        List<Course> courses;
        if (search != null && !search.isEmpty()) {
            courses = courseService.findCoursesByNameContaining(search);
        } else {
            courses = courseService.findAllCourses();
        }
        model.addAttribute("courses", courses);
        return "courses";
    }

}
