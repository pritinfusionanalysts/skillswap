package com.example.SkillSwap.repository;

import com.example.SkillSwap.entity.Skill;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SkillRepository extends JpaRepository<Skill,Long> {
}
