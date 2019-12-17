package ru.ert.account.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import ru.ert.account.model.Account;

import javax.persistence.LockModeType;
import java.util.List;

/**
 * Repository Account
 * @author kuyantsev
 * Date: 06.12.2019
 */
public interface AccountsRepository extends JpaRepository<Account, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    List<Account> save(List<Account> var1);
}
