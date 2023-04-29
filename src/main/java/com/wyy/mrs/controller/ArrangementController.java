package com.wyy.mrs.controller;

import com.wyy.mrs.model.entity.Arrangement;
import com.wyy.mrs.model.entity.Film;
import com.wyy.mrs.model.vo.ArrangementVO;
import com.wyy.mrs.service.ArrangementService;
import com.wyy.mrs.service.FilmService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
//电影排片场次接口
@RequestMapping("/api/arrangement")
public class ArrangementController {

    @Resource
    private ArrangementService arrangementService;

    @Resource
    private FilmService filmService;

    @PostMapping("")
    //新增电影场次
    public void save(@RequestBody Arrangement arrangement) {
        arrangementService.save(arrangement);
    }

    @PutMapping("")
    //修改排片信息
    public Arrangement update(@RequestBody Arrangement arrangement) {
        return arrangementService.Update(arrangement);
    }

    @DeleteMapping("")
    //根据id删除排片
    public void delete(@RequestParam String id) {
        arrangementService.deleteById(id);
    }

    @GetMapping("")
    //列出电影排片
    public List<Arrangement> list() {
        return arrangementService.findAll();
    }

    @GetMapping("/showing")
    //列出排片电影的信息
    public List<Film> ingList() {
        List<Arrangement> arrangementList = arrangementService.findAll();
        List<String> fidList = new ArrayList<>();
        List<Film> films = new ArrayList<>();
        for (Arrangement item : arrangementList ) {
            String fid = item.getFid();
            fidList.add(fid);
        }
        for (String fid : fidList) {
            Film film = filmService.findById(fid);
            films.add(film);
        }
        return films;
    }

    @GetMapping("/{id}")
    //查询排片
    public Map<String, Object> findById(@PathVariable String id) {
        HashMap<String, Object> map = new HashMap<>();
        Arrangement arrangement = arrangementService.findById(id);
        map.put("film", filmService.findById(arrangement.getFid()));
        map.put("arrangement", arrangement);
        return map;
    }

    @GetMapping("/getSeats")
    //获取座位情况
    public List<Integer> getSeats(String id) {
        return arrangementService.getSeatsHaveSelected(id);
    }

    @GetMapping("/film/{fid}")
    //查询某个电影的所有拍片
    public ArrangementVO findByFilmId(@PathVariable String fid) {
        return arrangementService.findByFilmId(fid);
    }

}
