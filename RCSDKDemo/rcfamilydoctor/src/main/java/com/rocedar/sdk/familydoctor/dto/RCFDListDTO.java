package com.rocedar.sdk.familydoctor.dto;


public class RCFDListDTO {
    private boolean isOpen;
    private boolean hasOpen;

    public boolean isHasOpen() {
        return hasOpen;
    }

    public void setHasOpen(boolean hasOpen) {
        this.hasOpen = hasOpen;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean open) {
        isOpen = open;
    }

    private RCFDDoctorListDTO rcfdDoctorListDTO;

    public RCFDDoctorListDTO getRcfdDoctorListDTO() {
        return rcfdDoctorListDTO;
    }

    public void setRcfdDoctorListDTO(RCFDDoctorListDTO rcfdDoctorListDTO) {
        this.rcfdDoctorListDTO = rcfdDoctorListDTO;
    }
}
