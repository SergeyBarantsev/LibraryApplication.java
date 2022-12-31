package com.sber.library.library.project.services;

import com.sber.library.library.project.exception.MyDeleteException;
import org.springframework.stereotype.Service;

import java.util.List;
public abstract class GenericService<T, N> {

    public abstract T update(T object);

    public abstract T updateFromDTO(N object, Long Id);

    public abstract T createFromDTO(N newDtoObject);

    public abstract T createFromEntity(T newObject);

    public abstract void delete(final Long objectId) throws MyDeleteException;

    public abstract T getOne(final Long objectId);

    public abstract List<T> listAll();


}
