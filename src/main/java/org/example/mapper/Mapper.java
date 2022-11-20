package org.example.mapper;

public interface Mapper<R, E> {
    R toRecord(E e);

    E toEntity(R r);
}
