package com.example.photobook.mapperToEntity;

public interface Mapper<T, U> {
    U toEntity(T t);
}
