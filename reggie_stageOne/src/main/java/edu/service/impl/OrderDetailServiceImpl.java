package edu.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.mapper.OrderDetailMapper;
import edu.pojo.OrderDetail;
import edu.service.OrderDetailService;
import org.springframework.stereotype.Service;

@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail> implements OrderDetailService {
}
