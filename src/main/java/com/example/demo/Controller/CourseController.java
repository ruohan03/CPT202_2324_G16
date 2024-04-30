package com.example.demo.Controller;
import com.example.demo.Model.Course;
import com.example.demo.Model.Staff;
import com.example.demo.Service.StaffService;
import com.example.demo.Service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.ArrayList;
import java.util.List;

@Controller
public class CourseController {
    @Autowired
    private CourseService courseService;
    @Autowired
    private StaffService staffService;


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
    @GetMapping("/manage_courses")
    public String manageCoursesList(@RequestParam(name = "search", required = false) String search, Model model, RedirectAttributes redirectAttributes) {
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
                return "redirect:/manage_courses";
            }
        } else {
            // 如果搜索信息不存在，则直接显示全部课程
            courses = courseService.findAllCourses();
        }
        model.addAttribute("courses", courses);
        return "manage_courses";
    }

    @GetMapping ("/delete_course/{id}")
    public ResponseEntity<String> deleteCourse(@PathVariable(value="id") String courseId) {
        if (!courseService.existsByCourseId(courseId)) {
            return ResponseEntity.badRequest().body("<script>alert('Course with ID " + courseId + " not found'); window.location.href='/manage_courses';</script>");
        }
        String confirmationMessage = "Are you sure you want to delete the course whose course ID is "+ courseId +" ? Once deleted, it cannot be withdrawn.";
        // 生成包含确认消息的JavaScript代码
        String confirmScript = "if(confirm('" + confirmationMessage + "')) { "
                + "window.location.href='/delete_course_confirmed/" + courseId + "';"
                + "} else {"
                + "window.location.href='/manage_courses';"
                + "}";
        // 返回包含确认消息的 JavaScript 响应
        return ResponseEntity.ok("<script>" + confirmScript + "</script>");
    }
    @GetMapping("/delete_course_confirmed/{id}")
    public ResponseEntity<String> deleteCourseConfirmed(@PathVariable(value="id") String courseId) {
        // 首先检查是否存在外键引用// 首先检查是否存在外键引用
        boolean hasForeignKey = courseService.hasForeignKey(courseId);
        // 如果存在，则将相关外键设置为NULL
        if (hasForeignKey) {
        List<Staff> staffList = staffService.findStaffByCourseId(courseId);
        for (Staff staff : staffList) {
            if (courseId.equals(staff.getCourseId())) {
                    staff.setCourseId(null);
                    staffService.saveOrUpdate(staff);
                }
            }
        }
        courseService.deleteCourse(courseId);
        // 根据是否存在外键引用返回不同的响应消息
        if (hasForeignKey) {
            return ResponseEntity.ok("<script>alert('Course with ID " + courseId + " and the course of the trainers who originally took the course has been cleared'); window.location.href='/manage_courses';</script>");
        } else {
            return ResponseEntity.ok("<script>alert('Course with ID " + courseId + " deleted successfully'); window.location.href='/manage_courses';</script>");
        }
    }

    @GetMapping("/add_course")
    public String addNewCoursePage() {
        return "add_course";
    }
    @PostMapping("/add_course")
    public ResponseEntity<String> addCourse(@ModelAttribute Course course) {
        if (course == null || course.getCourseId() == null || course.getName() == null) {
            return ResponseEntity.badRequest().body("<script>alert('Course ID and name are required'); window.location.href='/manage_courses';</script>");
        }

        if (!course.getCourseId().matches("^C\\d{3}$")) {
            return ResponseEntity.badRequest().body("<script>alert('Course ID must be Cxxx (x is number)'); window.location.href='/manage_courses';</script>");
        }

        if (courseService.existsByCourseId(course.getCourseId())) {
            return ResponseEntity.badRequest().body("<script>alert('Course with ID " + course.getCourseId() + " already exists'); window.location.href='/manage_courses';</script>");

        }

        courseService.addCourse(course);
        return ResponseEntity.ok("<script>alert('Course added successfully'); window.location.href='/manage_courses';</script>");
    }

    @GetMapping("/edit_course/{courseId}")
    public String showEditCourseForm(@PathVariable("courseId") String courseId, Model model) {
        Course course = courseService.findCourseById(courseId);
        if (course != null) {
            model.addAttribute("course", course);
            return "edit_course";
        } else {
            // Course not found, redirect or show error message
            return "redirect:/manage_courses";
        }
    }

    @PostMapping("/edit_course/{courseId}")
    public ResponseEntity<String> editCourse(@PathVariable("courseId") String courseId, @ModelAttribute Course updatedCourse) {
        if (courseService.existsByCourseId(courseId)) {
            // Set the courseId of the updated course to match the original courseId
            updatedCourse.setCourseId(courseId);
            courseService.editCourse(updatedCourse);
            return ResponseEntity.ok("<script>alert('Course updated successfully'); window.location.href='/manage_courses';</script>");
        } else {
            return ResponseEntity.badRequest().body("<script>alert('Course with ID " + courseId + " does not exist'); window.location.href='/manage_courses';</script>");
        }
    }
}
