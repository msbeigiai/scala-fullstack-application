import React from 'react';
import './App.css';
import { Form } from './components/form/Form';
import { SearchBar } from './components/searchBar/SearchBar';
import { BrowserRouter, Route, Routes } from 'react-router-dom';
import { IndexPage } from './pages/indexPage/IndexPage';
import { ThoughtsPage } from './pages/toughtsPages/ThoughtsPage';

function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path='/' element={<IndexPage />}></Route>
        <Route path='/new' element={<ThoughtsPage />}></Route>
      </Routes>
    </BrowserRouter>
  );
}

export default App;
