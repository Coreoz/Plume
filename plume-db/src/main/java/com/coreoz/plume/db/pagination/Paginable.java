package com.coreoz.plume.db.pagination;

import java.util.List;

/**
 * Permet de gérer simplement une pagination sur un ensemble d'objets.
 *
 * @author amanteaux
 *
 * @param <E>
 *            Le type d'élément qui est paginé
 */
public interface Paginable<E> {

	/**
	 * @return Le nombre d'éléments disponibles
	 */
	long count();

	/**
	 * @return La liste contenant l'ensemble des éléments disponibles
	 */
	List<E> fetch();

	/**
	 * Récupère une page de résultat
	 *
	 * @param page
	 *            La page cherchée, commence à 0
	 * @param pageSize
	 *            Le nombre d'élément par page
	 * @return La liste contenant la page demandée
	 * @throws IndexOutOfBoundsException
	 *             si page &lt; 0 ou si page*pageSize &gt; {@link #count()}
	 */
	List<E> fetch(int page, int pageSize);

}
