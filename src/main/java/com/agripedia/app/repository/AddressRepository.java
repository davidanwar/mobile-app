package com.agripedia.app.repository;

import com.agripedia.app.entity.AddressEntity;
import com.agripedia.app.entity.UserEntity;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AddressRepository extends PagingAndSortingRepository<AddressEntity, Long> {
    List<AddressEntity> findAllByUserDetails(UserEntity user);
    AddressEntity findByaddressId(String addressId);
}
