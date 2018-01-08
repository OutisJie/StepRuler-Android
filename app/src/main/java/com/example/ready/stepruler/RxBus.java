package com.example.ready.stepruler;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;


/**
 * Created by ready on 2017/12/10.
 */
//singleTown,只有一个实例化对象，只提供一个getInstance接口
public class RxBus {

    private ConcurrentHashMap<Object, List<Subject>> subjectMapper = new ConcurrentHashMap<>();
    private RxBus(){}
    @NonNull
    public static RxBus getInstance(){
        return Holder.instance;
    }
    @NonNull
    public <T>Observable<T> register(@NonNull Class<T> clz){
        return register(clz.getName());
    }
    @NonNull
    public <T> Observable<T> register(@NonNull Object tag) {
        List<Subject> subjectList = subjectMapper.get(tag);
        if (null == subjectList) {
            subjectList = new ArrayList<>();
            subjectMapper.put(tag, subjectList);
        }
        Subject<T> subject = PublishSubject.create();
        subjectList.add(subject);
        return subject;
    }
    public <T> void unregister(@NonNull Class<T> clz,@NonNull Observable observable){
        unregister(clz.getName(),observable);
    }
    public void unregister(@NonNull Object tag, @NonNull Observable observable){
        List<Subject> subjects = subjectMapper.get(tag);
        if(subjects != null){
            subjects.remove(observable);
            if(subjects.isEmpty()){
                subjectMapper.remove(tag);
            }
        }
    }
    public void post(@NonNull Object content){
        post(content.getClass().getName(), content);
    }
    public void post(@NonNull Object tag, @NonNull Object content){
        List<Subject> subjects = subjectMapper.get(tag);
        if(!subjects.isEmpty()){
            for(Subject subject : subjects){
                subject.onNext(content);
            }
        }
    }

    private static class Holder{
        private static RxBus instance = new RxBus();
    }
}
