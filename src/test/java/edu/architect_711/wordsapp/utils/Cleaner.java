package edu.architect_711.wordsapp.utils;

import edu.architect_711.wordsapp.repository.AccountRepository;
import edu.architect_711.wordsapp.repository.GroupRepository;
import edu.architect_711.wordsapp.repository.NodeRepository;
import edu.architect_711.wordsapp.repository.WordRepository;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Builder
public class Cleaner {
    private GroupRepository groupRepository;
    private AccountRepository accountRepository;
    private NodeRepository nodeRepository;
    private WordRepository wordRepository;

    public void clear(Long groupId, Long accountId, Long account2Id) {
        groupRepository.deleteById(groupId);
        accountRepository.deleteById(accountId);
        accountRepository.deleteById(account2Id);
    }

    public void clear(Long groupId, Long accountId) {
        groupRepository.deleteById(groupId);
        accountRepository.deleteById(accountId);
    }

    public void clear(Long nodeId, Long wordId, Long groupId, Long accountId) {
        nodeRepository.deleteById(nodeId);
        wordRepository.deleteById(wordId);
        groupRepository.deleteById(groupId);
        accountRepository.deleteById(accountId);
    }

}
