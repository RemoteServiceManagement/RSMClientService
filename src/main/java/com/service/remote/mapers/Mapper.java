package com.service.remote.mapers;

/**
 * Created by Dawid on 02.05.2018 at 11:39.
 */
public interface Mapper <R,T> {
    R map(T t);
}
