package edu.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.mapper.AddressBookMapper;
import edu.pojo.AddressBook;
import edu.service.AddressBookService;
import org.springframework.stereotype.Service;

@Service
public class AdressBookServiceImpl extends ServiceImpl<AddressBookMapper, AddressBook> implements AddressBookService {
}
