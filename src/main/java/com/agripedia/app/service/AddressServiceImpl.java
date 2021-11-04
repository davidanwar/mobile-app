package com.agripedia.app.service;

import com.agripedia.app.entity.AddressEntity;
import com.agripedia.app.entity.UserEntity;
import com.agripedia.app.repository.AddressRepository;
import com.agripedia.app.repository.UserRepository;
import com.agripedia.app.shared.dto.AddressDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AddressServiceImpl implements AddressService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    AddressRepository addressRepository;

    @Override
    public List<AddressDto> getAddresses(String userId) {
        List<AddressDto> returnValue = new ArrayList<>();
        UserEntity user = userRepository.findByUserId(userId);
        if (user == null) return returnValue;
        ModelMapper mapper = new ModelMapper();
        Iterable<AddressEntity> addressEntities = addressRepository.findAllByUserDetails(user);
        for (AddressEntity address : addressEntities) {
            returnValue.add(mapper.map(address, AddressDto.class));
        }
        return returnValue;
    }

    @Override
    public AddressDto getAddress(String addressId) {
        AddressDto returnValue = null;
        ModelMapper mapper = new ModelMapper();
        AddressEntity address = addressRepository.findByaddressId(addressId);
        if (address != null) {
            returnValue = mapper.map(address, AddressDto.class);
        }
        return returnValue;
    }
}
