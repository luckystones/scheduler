package com.ugur.mapper;

import com.ugur.model.Model;
import org.mapstruct.AfterMapping;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractMapper<M extends Model, E> {

  public List<E> mapToEntityList(final List<M> mList) {
    final List<E> eList = new ArrayList<>();
    if (mList == null) {
      return eList;
    }
    for (final M m : mList) {
      eList.add(mapToEntity(m));
    }
    return eList;
  }

  public List<M> mapToModelList(final List<E> eList) {
    final List<M> mList = new ArrayList<>();
    if (eList == null) {
      return mList;
    }
    for (final E e : eList) {
      mList.add(mapToModel(e));
    }
    return mList;
  }

  abstract M mapToModel(E object);

  abstract E mapToEntity(M object);



}
