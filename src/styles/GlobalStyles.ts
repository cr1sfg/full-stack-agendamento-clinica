import styled, { createGlobalStyle } from 'styled-components';
import minhaImagem from '../images/backgroundImage.png';

export const GlobalStyles = createGlobalStyle`
  * {
    margin: 0;
    padding: 0;
    box-sizing: border-box;
  }

  body {
    width: 100%;
    height: 100vh;
    background-image: url(${minhaImagem});
    background-size: cover;
    background-position: center;
    background-repeat: no-repeat;
    display: flex;
    justify-content: center;
    align-items: center;
  }
`

export const ContainerFormulario = styled.div`
  width: 980px;
  height: 640px;
  background-color: #ffffffd0;
  text-align: center;
  box-shadow: 5px 5px 10px rgba(0, 0, 0, 0.5);
  padding: 32px;
`

export const FormularioPadrao = styled.form`
  padding-top: 48px; 

  input {
    margin-top: 32px;
    width: 100%;
    padding: 24px;
    text-align: center;
    font-size: 16px;
    border: none;
    background-color: transparent;
    border-color: #ccc;

    &:focus {
      outline: none;
      border: 1px solid #ccc;
    }
  }
`

export const Titulo = styled.h1`
    padding-bottom: 16px;
`

export const Botao = styled.button`
    background-color: transparent;
    padding: 16px;
    width: 100%;
    border-color: #fff;
    cursor: pointer;
    outline: none;
    font-size: 18px;
    margin-top: 32px;

    &:hover {
        background-color: #f8f4f45e;
    }
`

export const BotaoFormulario = styled.button`
  background-color: transparent;
  justify-content: space-between;
  padding: 16px 8px;
  width: 45%;
  margin-left: 10px;
    border-color: #fff;
    cursor: pointer;
    outline: none;
    font-size: 18px;
    margin-top: 64px;

    &:hover {
        background-color: #f8f4f45e;
    }
`
export default GlobalStyles;