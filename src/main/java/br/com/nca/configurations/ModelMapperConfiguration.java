package br.com.nca.configurations;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import br.com.nca.domain.dtos.AlterarProdutoDTO;
import br.com.nca.domain.dtos.CriarProdutoDTO;
import br.com.nca.domain.dtos.ObterProdutoDTO;
import br.com.nca.domain.entities.Produto;

@Configuration
public class ModelMapperConfiguration {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper mapper = new ModelMapper();

        mapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.STRICT);

        mapper.typeMap(Produto.class, ObterProdutoDTO.class);

        mapper.typeMap(CriarProdutoDTO.class, Produto.class)
                .addMapping(CriarProdutoDTO::getNome, Produto::setNome)
                .addMapping(CriarProdutoDTO::getTipo, Produto::setTipo)
                .addMapping(CriarProdutoDTO::getPrecoUnitario, Produto::setPrecoUnitario);

        mapper.typeMap(AlterarProdutoDTO.class, Produto.class)
                .addMapping(AlterarProdutoDTO::getId, Produto::setId)
                .addMapping(AlterarProdutoDTO::getNome, Produto::setNome)
                .addMapping(AlterarProdutoDTO::getTipo, Produto::setTipo)
                .addMapping(AlterarProdutoDTO::getPrecoUnitario, Produto::setPrecoUnitario);

        return mapper;
    }
}