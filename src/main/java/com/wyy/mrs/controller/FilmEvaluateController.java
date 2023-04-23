package com.wyy.mrs.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wyy.mrs.mapper.UserMapper;
import com.wyy.mrs.model.entity.FilmEvaluate;
import com.wyy.mrs.model.entity.User;
import com.wyy.mrs.model.vo.FilmEvaluateVO;
import com.wyy.mrs.service.CollaborativeFilteringService;
import com.wyy.mrs.service.FilmEvaluateService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
//电影评价接口
@RequestMapping("/api/fe")
public class FilmEvaluateController {

    @Resource
    UserMapper userMapper;
    @Resource
    private FilmEvaluateService filmEvaluateService;
    @Resource
    private CollaborativeFilteringService collaborativeFilteringService;

    @PostMapping("")
    //评论电影
    public void save(@RequestBody FilmEvaluate filmEvaluate) throws Exception {
        filmEvaluateService.save(filmEvaluate);

        //设置用户标志位，代表评价过film
        User user = userMapper.selectOne(
                new QueryWrapper<User>()
                        .eq("id", filmEvaluate.getUid())
        );
        if (user.getIsEvaluate().equals(0)) {
            user.setIsEvaluate(1);
            userMapper.updateById(user);
            System.out.println("设置用户标志位，代表评价过film");
        }

        //更新CollaborativeFiltering数据
        //1、查询所有FilmEvaluate的list
        List<FilmEvaluate> allFilm = filmEvaluateService.findAllFilm();
        //2、更新数据
        collaborativeFilteringService.updateValue(allFilm);
    }

    @GetMapping("")
    //获取电影评论
    public List<FilmEvaluateVO> list(@RequestParam(name = "fid") String fid) {
        if (fid != null) {
            return filmEvaluateService.findAllByFilmId(fid);
        }
        return null;
    }

    @DeleteMapping("/{id}")
    //根据id删除评论
    public void remove(@PathVariable String id) {
        filmEvaluateService.deleteById(id);
    }

    @DeleteMapping("")
    //删除该电影的所有评分
    public void removeAll(@RequestParam(name = "fid") String fid) {
        filmEvaluateService.deleteAllByFilmId(fid);
    }

}
