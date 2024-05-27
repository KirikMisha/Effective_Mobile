package org.example.effectivemobile.service;

import org.example.effectivemobile.dto.SearchRequest;
import org.example.effectivemobile.dto.SearchResult;
import org.example.effectivemobile.models.BankAccounts;
import org.example.effectivemobile.models.Users;
import org.example.effectivemobile.repositories.BankAccountsRepository;
import org.example.effectivemobile.repositories.UsersRepository;
//import org.example.effectivemobile.service.interfaceService.SearchService;
//import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SearchServiceImpl{

    private final UsersRepository usersRepository;

    private final BankAccountsRepository bankAccountsRepository;

    public SearchServiceImpl(UsersRepository usersRepository, BankAccountsRepository bankAccountsRepository) {
        this.usersRepository = usersRepository;
        this.bankAccountsRepository = bankAccountsRepository;
    }

    public SearchResult search(SearchRequest request) {
        String fullName = request.getFullName();
        String phoneNumber = request.getPhoneNumber();
        String email = request.getEmail();
        String dateOfBirth = request.getDateOfBirth();
        int page = request.getPage();
        int size = request.getSize();
        String sortBy = request.getSortBy() != null ? request.getSortBy() : "fullName";
        Sort.Direction sortOrder = "desc".equalsIgnoreCase(request.getSortOrder()) ? Sort.Direction.DESC : Sort.Direction.ASC;

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortOrder, sortBy));

        System.out.println(pageable);

        Page<Users> usersPage = usersRepository.searchUsers(fullName, phoneNumber, email, dateOfBirth, pageable);

        List<Users> users = usersPage.getContent();

        List<SearchResult.UserInfo> userInfos = users.stream().map(this::mapToUserInfo).collect(Collectors.toList());

        SearchResult result = new SearchResult();
        result.setUsers(userInfos);
        result.setTotalPages(usersPage.getTotalPages());
        result.setTotalElements(usersPage.getTotalElements());

        return result;
    }

    private SearchResult.UserInfo mapToUserInfo(Users user) {
        SearchResult.UserInfo userInfo = new SearchResult.UserInfo();
        userInfo.setFullName(user.getFullName());
        userInfo.setPhoneNumber(user.getPhoneNumber());

        BankAccounts bankAccount = bankAccountsRepository.findByUser(user);
        if (bankAccount != null) {
            userInfo.setBalance(bankAccount.getInitialBalance());
        } else {
            userInfo.setBalance(0.0);
        }

        return userInfo;
    }
}
