package com.ontag.mcash.admin.web.service;

import java.util.List;

import com.ontag.mcash.admin.web.exception.ServiceException;
import com.ontag.mcash.dal.exception.DataAccessException;


public interface GenericService<T> {

	
    ServiceException translateException(DataAccessException de);

    T getById(int id) throws ServiceException;

    T add(T obj) throws ServiceException;

    T edit(T obj) throws ServiceException;

    List<Object> getList(int offset, int size) throws ServiceException;

    void revoke(Integer id) throws ServiceException;

    void revokeAll(Integer[] ids) throws ServiceException;

    int countAll() throws ServiceException;

    List<T> getAll() throws ServiceException;

}
