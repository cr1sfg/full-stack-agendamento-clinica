import styled from 'styled-components'

export const Principal = styled.aside`
  width: 620px;
  height: 640px;
  background-color: #ffffffd0;
  text-align: center;
  box-shadow: 5px 5px 10px rgba(0, 0, 0, 0.5);
  padding: 32px;
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