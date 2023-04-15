import React from 'react';
import { SearchBarProb } from '../../utils/utils';
import styles from './SearchBar.module.scss';

export const SearchBar = ({placeHolder}: SearchBarProb) => {
    return(
        <form className={styles.search}>
            <input type="search" placeholder={placeHolder} className={styles.children}/>
            <input type="submit" className={styles.submit}/>
        </form>
    )
}