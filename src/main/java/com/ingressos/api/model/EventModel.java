package com.ingressos.api.model;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.hateoas.RepresentationModel;

import com.ingressos.api.enums.EventStatus;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

@Entity
@Table(name = "tb_events", catalog = "database_plataforma_evento")
public class EventModel extends RepresentationModel<EventModel> implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tb_events")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "tb_events_fk_id_tb_users", referencedColumnName = "id_tb_users", nullable = false)
    private UserModel user;

    @ManyToOne
    @JoinColumn(name = "tb_events_fk_id_tb_addresses", referencedColumnName = "id_tb_addresses", nullable = false)
    private AddresseModel address;

    @Column(name = "tb_events_name", nullable = false)
    private String name;

    @Column(name = "tb_events_description")
    private String description;

    @Column(name = "tb_events_banner_image_url")
    private String bannerImageUrl;

    @ElementCollection
    @Column(name = "gallery_image_url")
    private List<String> galleryImages;

    @Column(name = "tb_events_start_date", nullable = false)
    private LocalDateTime startDate;

    @Column(name = "tb_events_end_date", nullable = false)
    private LocalDateTime endDate;

    @Column(name = "tb_events_max_capacity")
    private Integer maxCapacity;

    @Column(name = "tb_events_current_capacity", nullable = false)
    private Integer currentCapacity = 0;

    @Column(name = "tb_events_base_price", precision = 10, scale = 2)
    private BigDecimal basePrice;

    @Column(name = "tb_events_category")
    private String category;

    @Column(name = "tb_events_tags")
    private String tags; // JSON ou string separada por vírgulas

    @Enumerated(EnumType.STRING)
    @Column(name = "tb_events_status", nullable = false)
    private EventStatus status = EventStatus.DRAFT;

    @Column(name = "tb_events_is_featured")
    private Boolean isFeatured = false;

    @Column(name = "tb_events_is_online")
    private Boolean isOnline = false;

    @Column(name = "tb_events_online_url")
    private String onlineUrl;

    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL)
    private List<TicketTypeModel> ticketTypes;

    @Column(name = "tb_events_created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "tb_events_updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public EventModel() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public UserModel getUser() {
        return user;
    }

    public void setUser(UserModel user) {
        this.user = user;
    }

    public AddresseModel getAddress() {
        return address;
    }

    public void setAddress(AddresseModel address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBannerImageUrl() {
        return bannerImageUrl;
    }

    public void setBannerImageUrl(String bannerImageUrl) {
        this.bannerImageUrl = bannerImageUrl;
    }

    public List<String> getGalleryImages() {
        return galleryImages;
    }

    public void setGalleryImages(List<String> galleryImages) {
        this.galleryImages = galleryImages;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public Integer getMaxCapacity() {
        return maxCapacity;
    }

    public void setMaxCapacity(Integer maxCapacity) {
        this.maxCapacity = maxCapacity;
    }

    public Integer getCurrentCapacity() {
        return currentCapacity;
    }

    public void setCurrentCapacity(Integer currentCapacity) {
        this.currentCapacity = currentCapacity;
    }

    public BigDecimal getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(BigDecimal basePrice) {
        this.basePrice = basePrice;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public EventStatus getStatus() {
        return status;
    }

    public void setStatus(EventStatus status) {
        this.status = status;
    }

    public Boolean getIsFeatured() {
        return isFeatured;
    }

    public void setIsFeatured(Boolean isFeatured) {
        this.isFeatured = isFeatured;
    }

    public Boolean getIsOnline() {
        return isOnline;
    }

    public void setIsOnline(Boolean isOnline) {
        this.isOnline = isOnline;
    }

    public String getOnlineUrl() {
        return onlineUrl;
    }

    public void setOnlineUrl(String onlineUrl) {
        this.onlineUrl = onlineUrl;
    }

    public List<TicketTypeModel> getTicketTypes() {
        return ticketTypes;
    }

    public void setTicketTypes(List<TicketTypeModel> ticketTypes) {
        this.ticketTypes = ticketTypes;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
