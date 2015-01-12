package com.indra.infra.service;

import java.io.Serializable;
import java.util.List;

import com.indra.infra.service.exception.ApplicationException;

/**
 * Interface contendo as operações basicas a serem realidas pelos services e acesso a banco de dados
 * 
 * @author raphael.galvao
 *
 * @param <T>
 * 			Entidade sobre a qual serão realizadas as operações 
 */
public interface BaseService<T> extends Serializable {
	
	/**
	 * Insere a entidade no banco de dados
	 * 
	 * @param entity
	 * 		A entidade que será persistida no banco
	 * 
	 * @return
	 * 		A entidade que foi persistida no banco
	 */
	T save(T entity) throws ApplicationException;
	
	/**
	 * Atualiza a entidade no banco de dados
	 * 
	 * @param entity
	 * 		A entidade que será atualizada no banco
	 * 
	 * @return
	 * 		A entidade que foi atualizada no banco
	 * 
	 * @throws ApplicationException 
	 */
	T update(T entity) throws ApplicationException;

	/**
	 * Lista todas as entidades cadastradas na tabela que representa a entidade que está manipulando.
	 * 
	 * @return
	 * 		Coleção contendo todas as entidades cadastrasdas na tabela que representa a entidade que está manipulando.
	 */
	List<T> findAll();

	/**
	 * Recupera a entidade a partir de seu identificador
	 * 
	 * @param id
	 * 		Identificador da entidade
	 * 
	 * @return
	 * 		A entidade com seus dados preenchidos obtidos no banco de dados
	 * 
	 * @throws ApplicationException 
	 */
	T findById(Long id) throws ApplicationException;
	
	/**
	 * Exclui a entidade do banco de dados
	 * 
	 * @param id
	 * 		Identificador da entidade
	 * 
	 * @throws ApplicationException 
	 */
	void remove(Long id) throws ApplicationException;
	
	/**
	 * Exclui a entidade do banco de dados
	 * 
	 * @param ids
	 * 		Identificadores da entidade
	 * 
	 * @throws ApplicationException 
	 */
	void remove(List<Long> ids) throws ApplicationException;
	
}
