package br.com.brasilprevteste.service.impl;

import br.com.brasilprevteste.errorValidate.ErroMessage;
import br.com.brasilprevteste.model.Produto.Produto;
import br.com.brasilprevteste.repository.Filter.ProdutoFilter;
import br.com.brasilprevteste.repository.produto.ProdutoRepository;
import br.com.brasilprevteste.service.ProdutoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Slf4j // TODO: criar um log para os métodos
@Service
public class ProdutoServiceImpl extends ErroMessage implements ProdutoService {

        @Autowired
        private ProdutoRepository repository;

        @Override
        public Page<Produto> filtrar(ProdutoFilter produtoFilter, Pageable pageable) {
            return repository.filtrar(produtoFilter, pageable);
        }

        @Override
        public Produto salvar(Produto produto) {
            return repository.save(produto);
        }

        @Override
        public Produto atualizar(Long id, Produto produto) {
            Produto produtoSalvo = repository.findById(id).orElseThrow(() -> notFouldId(id, "o produto"));
            BeanUtils.copyProperties(produto, produtoSalvo, "id");
            return repository.save(produtoSalvo);
        }

        @Override
        public Produto deletar(Long id) {
            Produto produtoSalvo = repository.findById(id).orElseThrow(() -> notFouldId(id, "o produto"));
            repository.deleteById(id);
            return produtoSalvo;
        }

        @Override
        public Produto detalhar(Long id) {
            return repository.findById(id).orElseThrow(() -> notFouldId(id, "o produto"));
        }
}
