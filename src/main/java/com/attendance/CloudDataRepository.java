package com.attendance;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CloudDataRepository extends JpaRepository<CloudData, String> {
}
